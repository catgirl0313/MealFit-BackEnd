package com.mealfit.unit.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mealfit.common.email.EmailUtil;
import com.mealfit.common.factory.UserFactory;
import com.mealfit.common.storageService.StorageService;
import com.mealfit.exception.user.NoUserException;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserStatus;
import com.mealfit.user.dto.request.ChangeUserInfoRequestDto;
import com.mealfit.user.dto.response.UserInfoResponseDto;
import com.mealfit.user.repository.EmailCertificationRepository;
import com.mealfit.user.repository.UserRepository;
import com.mealfit.user.service.UserInfoService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("UserInfoServiceTest - 회원 정보 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class UserInfoServiceTest {

    @InjectMocks
    private UserInfoService userInfoService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailUtil emailUtil;

    @Mock
    private EmailCertificationRepository emailCertificationRepository;

    @Mock
    private StorageService storageService;

    @DisplayName("findUserInfo() 메서드는")
    @Nested
    class Testing_findUserInfo {

        @DisplayName("로그인이 되었을 때")
        @Nested
        class Context_Login {

            @DisplayName("자신의 프로필을 조회할 수 있다.")
            @Test
            void getUserInfo_WithMyUsername_Success() {
                User user = UserFactory.createSaveUser("test1", "qwe123!!" ,
                      "test@gmail.com", "nickname1", UserStatus.NORMAL);

                String loginUsername = "test1";

                // given
                given(userRepository.findByUsername(loginUsername)).willReturn(Optional.ofNullable(user));

                // when
                UserInfoResponseDto userResponseDto = userInfoService.findUserInfo("test1");

                // then
                assertThat(userResponseDto).isNotNull();
                verify(userRepository, times(1))
                      .findByUsername("test1");
            }
        }

        @DisplayName("로그인이 되어있지 않을 때")
        @Nested
        class Context_Not_Login {

            @DisplayName("자신의 프로필을 조회할 수 없다.")
            @Test
            void getUserInfo_WithMyUsername_UnSuccess() {
                User user = UserFactory.createSaveUser("test1", "qwe123!!" ,
                      "test@gmail.com", "nickname1", UserStatus.NORMAL);

                String notLoginUsername = null;

                // when then
                assertThatThrownBy(() -> userInfoService.findUserInfo(notLoginUsername))
                      .isInstanceOf(NoUserException.class);

                verify(userRepository, times(1))
                      .findByUsername(null);
            }
        }
    }

    @DisplayName("changeUserInfo() 메서드는")
    @Nested
    class Testing_changeUserInfo {
        @DisplayName("로그인이 되었을 때")
        @Nested
        class Context_Login {

            @DisplayName("자신의 프로필을 수정할 수 있다.")
            @Test
            void changeUserInfo_WithMyUsername_Success() {
                User user = UserFactory.createSaveUser("test1", "qwe123!!" ,
                      "nickname1", "test@gmail.com", UserStatus.NORMAL);

                String loginUsername = "test1";

                ChangeUserInfoRequestDto requestDto = UserFactory.createChangeUserInfoRequestDto(
                      "newNickname1",
                      null,
                      80,
                      70);

                // given
                given(userRepository.findByUsername(loginUsername)).willReturn(Optional.ofNullable(user));

                // when
                UserInfoResponseDto responseDto = userInfoService.changeUserInfo("test1", requestDto);

                // then
                // 나중에 일치시킬 수 있다면 usingRecursiveComparison() 사용도 나쁘지 않다.
                assertThat(requestDto.getNickname()).isEqualTo(responseDto.getUserProfile().getNickname());
                assertThat(requestDto.getGoalWeight()).isEqualTo(responseDto.getUserProfile().getGoalWeight());

                verify(userRepository, times(1))
                      .findByUsername("test1");
            }
        }

        @DisplayName("로그인이 되어있지 않을 때")
        @Nested
        class Context_Not_Login {

            @DisplayName("자신의 프로필을 수정할 수 없다.")
            @Test
            void getUserInfo_WithMyUsername_UnSuccess() {
                ChangeUserInfoRequestDto requestDto = UserFactory.createChangeUserInfoRequestDto(
                      "newNickname1",
                      null);

                String notLoginUsername = null;

                // given
                given(userRepository.findByUsername(notLoginUsername)).willReturn(Optional.empty());

                // when then
                assertThatThrownBy(() -> userInfoService.changeUserInfo(notLoginUsername, requestDto))
                      .isInstanceOf(NoUserException.class);

                verify(userRepository, times(1))
                      .findByUsername(null);
            }
        }
    }
}
