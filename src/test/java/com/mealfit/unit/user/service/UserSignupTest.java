package com.mealfit.unit.user.service;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.mealfit.common.email.EmailUtil;
import com.mealfit.common.factory.UserFactory;
import com.mealfit.exception.user.DuplicatedSignUpException;
import com.mealfit.exception.user.PasswordCheckException;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserStatus;
import com.mealfit.user.dto.request.SignUpRequestDto;
import com.mealfit.user.repository.EmailCertificationRepository;
import com.mealfit.user.repository.UserRepository;
import com.mealfit.user.service.UserSignUpService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@DisplayName("UserSignupServiceTest - 회원가입 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class UserSignupTest {

    private static final String DUPLICATION_USERNAME = "이미 존재하는 아이디입니다.";
    private static final String DUPLICATION_NICKNAME = "이미 존재하는 닉네임입니다.";
    private static final String DUPLICATION_EMAIL = "이미 존재하는 이메일입니다.";
    private static final String PASSWORD_WRONG_PATTERN = "비밀번호는 최소 8자리에 숫자, 영어 문자를 각각 1개 이상 포함해야 합니다.";
    private static final String PASSWORD_NOT_MATCHED_MESSAGE = "비밀번호와 비밀번호 재확인이 일치하지 않습니다.";

    @InjectMocks
    private UserSignUpService userSignUpService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailUtil emailUtil;

    @Mock
    private EmailCertificationRepository emailCertificationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @DisplayName("signup() 메서드는")
    @Nested
    class Testing_signUp {

        @DisplayName("아이디가")
        @Nested
        class Context_Username_Validation {

            @DisplayName("중복되면 오류가 발생한다.")
            @Test
            void username_duplicate_unSuccess() {

                // given
                String duplicatedUsername = "test";
                given(userRepository.existsByUsername(duplicatedUsername)).willReturn(true);

                // when

                // then
                assertThatThrownBy(() -> userSignUpService.validateUsername(duplicatedUsername))
                      .isInstanceOf(DuplicatedSignUpException.class)
                      .hasMessage(DUPLICATION_USERNAME);
            }

            @DisplayName("중복되지 않으면 통과한다.")
            @Test
            void username_NOT_duplicate_success() {

                // given
                String notDuplicatedUsername = "test1";

                // then
                assertThatNoException().isThrownBy(
                      () -> userSignUpService.validateUsername(notDuplicatedUsername));
            }
        }

        @DisplayName("닉네임이")
        @Nested
        class Context_Nickname_Validation {

            @DisplayName("중복되면 오류가 발생한다.")
            @Test
            void nickname_duplicate_unSuccess() {

                //given
                String savedNickname = "nickname";
                String duplicateNickname = "nickname";

                // when
                given(userRepository.existsByNickname(savedNickname)).willReturn(true);

                // then
                assertThatThrownBy(() -> userSignUpService.validateNickname(duplicateNickname))
                      .isInstanceOf(DuplicatedSignUpException.class)
                      .hasMessage(DUPLICATION_NICKNAME);
            }

            @DisplayName("중복되지 않으면 통과한다.")
            @Test
            void nickname_NOT_duplicate_success() {
                // given
                String notDuplicateNickname = "nickname";

                // then
                assertThatNoException().isThrownBy(
                      () -> userSignUpService.validateNickname(notDuplicateNickname));
            }
        }

        @DisplayName("이메일이")
        @Nested
        class Context_Email_Validation {

            @DisplayName("중복되면 오류가 발생한다.")
            @Test
            void email_duplicate_unSuccess() {
                // given
                String duplicateEmail = "test@gmail.com";

                // when
                when(userRepository.existsByEmail(duplicateEmail)).thenReturn(true);

                // then
                assertThatThrownBy(() -> userSignUpService.validateEmail(duplicateEmail))
                      .isInstanceOf(DuplicatedSignUpException.class)
                      .hasMessage(DUPLICATION_EMAIL);
            }

            @DisplayName("중복되지 않으면 통과한다.")
            @Test
            void email_NOT_duplicate_success() {

                // given
                String notDuplicateEmail = "test1@gmail.com";

                // then
                assertThatNoException().isThrownBy(
                      () -> userSignUpService.validateEmail(notDuplicateEmail));
            }
        }

        @DisplayName("비밀번호가")
        @Nested
        class Context_Password_Validation {

            @DisplayName("8자리 이하면 오류가 발생한다.")
            @Test
            void password_too_short_unSuccess() {
                String tooShort = "qwe123";

                assertThatThrownBy(() -> userSignUpService.validatePassword(tooShort, tooShort))
                      .isInstanceOf(PasswordCheckException.class)
                      .hasMessage(PASSWORD_WRONG_PATTERN);
            }

            @DisplayName("영어 숫자를 최소한 1개 이상 포함하지 않으면 오류가 발생한다.")
            @Test
            void password_Not_include_number_english_unSuccess() {
                String onlyEnglish = "qweqweeqw";
                String onlyNumber = "12312312321";

                assertThatThrownBy(
                      () -> userSignUpService.validatePassword(onlyEnglish, onlyEnglish))
                      .isInstanceOf(PasswordCheckException.class)
                      .hasMessage(PASSWORD_WRONG_PATTERN);

                assertThatThrownBy(() -> userSignUpService.validatePassword(onlyNumber, onlyNumber))
                      .isInstanceOf(PasswordCheckException.class)
                      .hasMessage(PASSWORD_WRONG_PATTERN);
            }

            @DisplayName("비밀번호 재입력과")
            @Nested
            class Context_PasswordCheck_Validation {

                @DisplayName("일치하지 않으면 오류가 발생한다.")
                @Test
                void NOT_same_with_passwordCheck_unSuccess() {
                    String password1 = "qwe11111";
                    String notMatchedPassword = "qwe22222";

                    assertThatThrownBy(
                          () -> userSignUpService.validatePassword(password1, notMatchedPassword))
                          .isInstanceOf(PasswordCheckException.class)
                          .hasMessage(PASSWORD_NOT_MATCHED_MESSAGE);
                }

                @Test
                void same_with_passwordCheck_Success() {
                    String password1 = "qwe11111";
                    String MatchedPassword = "qwe11111";

                    assertThatNoException().isThrownBy(() ->
                          userSignUpService.validatePassword(password1, MatchedPassword));
                }
            }
        }

        @DisplayName("모두 일치하면 성공한다.")
        @Test
        void all_pass_success() {

            String username = "test";

            User saveUser = UserFactory.createMockLocalUser(1L, username, "qwe123123",
                  "nickname", "test@gmail.com", UserStatus.NORMAL);

            // given
            SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                  .username(username)
                  .email("test@gmail.com")
                  .password("qwe123123")
                  .passwordCheck("qwe123123")
                  .nickname("nickname")
                  .build();

            // when
            given(userRepository.save(any(User.class))).willReturn(saveUser);

            User savedUser = userSignUpService.signup("TEST_DOMAIN_URL", signUpRequestDto);

            // then
            assertEquals(signUpRequestDto.getUsername(), savedUser.getUsername());
            assertEquals(signUpRequestDto.getEmail(), savedUser.getEmail());
            assertEquals(signUpRequestDto.getPassword(), savedUser.getPassword());
            assertEquals(signUpRequestDto.getNickname(), savedUser.getNickname());
        }
    }
}