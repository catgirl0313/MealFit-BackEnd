package com.mealfit.user.service;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.mealfit.user.dto.request.SignUpRequestDto;
import com.mealfit.user.repository.UserRepository;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class UserValidationServiceTest {

    @Autowired
    private UserValidationService userValidationService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
              .username("test")
              .email("test@gmail.com")
              .password("qwe123123")
              .passwordCheck("qwe123123")
              .nickname("test1")
              .profileImage(null)
              .currentWeight(80.0)
              .goalWeight(75.1)
              .startFasting(LocalTime.now())
              .endFasting(LocalTime.now())
              .build();

        userRepository.save(signUpRequestDto.toEntity());
    }

    @DisplayName(value = "아이디 중복 체크")
    @Test
    void 아이디_중복_체크() {
        String duplicateUsername = "test";
        String notDuplicateUsername = "test1";

        assertThatIllegalArgumentException()
              .isThrownBy(() -> userValidationService.validateUsername(duplicateUsername))
              .withMessage("이미 존재하는 아이디입니다.");

        assertThatNoException().isThrownBy(() -> userValidationService.validateUsername(notDuplicateUsername));
    }

    @DisplayName(value = "닉네임 중복 체크")
    @Test
    void 닉네임_중복_체크() {
        String duplicateNickname = "test1";
        String notDuplicateNickname = "test2";

        assertThatIllegalArgumentException()
              .isThrownBy(() -> userValidationService.validateNickname(duplicateNickname))
              .withMessage("이미 존재하는 닉네임입니다.");

        assertThatNoException().isThrownBy(() -> userValidationService.validateNickname(notDuplicateNickname));
    }

    @DisplayName(value = "이메일 중복 체크")
    @Test
    void 이메일_중복_체크() {
        String duplicateEmail = "test@gmail.com";
        String notDuplicateEmail = "test1@gmail.com";

        assertThatIllegalArgumentException()
              .isThrownBy(() -> userValidationService.validateEmail(duplicateEmail))
              .withMessage("이미 존재하는 이메일입니다.");

        assertThatNoException().isThrownBy(() -> userValidationService.validateEmail(notDuplicateEmail));
    }

    @DisplayName(value = "비밀번호 일치여부 체크")
    @Test
    void 비밀번호_일치여부_체크() {
        String tooShort = "qwe123";
        String onlyEnglish = "qweqweeqw";
        String onlyNumber = "12312312321";
        String password1 = "qwe11111";
        String password2 = "qwe22222";
        String password3 = "qwe11111";

        assertThatIllegalArgumentException().isThrownBy(() ->
                    userValidationService.validatePassword(tooShort, password2))
              .withMessage("비밀번호는 최소 8자리에 숫자, 영어 문자를 각각 1개 이상 포함해야 합니다.");

        assertThatIllegalArgumentException().isThrownBy(() ->
                    userValidationService.validatePassword(onlyEnglish, password2))
              .withMessage("비밀번호는 최소 8자리에 숫자, 영어 문자를 각각 1개 이상 포함해야 합니다.");

        assertThatIllegalArgumentException().isThrownBy(() ->
                    userValidationService.validatePassword(onlyNumber, password2))
              .withMessage("비밀번호는 최소 8자리에 숫자, 영어 문자를 각각 1개 이상 포함해야 합니다.");

        assertThatIllegalArgumentException().isThrownBy(() ->
              userValidationService.validatePassword(password1, password2))
              .withMessage("패스워드가 일치하지 않습니다.");

        assertThatNoException().isThrownBy(() ->
              userValidationService.validatePassword(password1, password3));
    }
}