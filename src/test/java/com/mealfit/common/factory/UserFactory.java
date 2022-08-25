package com.mealfit.common.factory;

import com.mealfit.user.domain.ProviderType;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserStatus;
import com.mealfit.user.controller.dto.request.UserSignUpRequest;
import com.mealfit.user.controller.dto.request.ChangeUserInfoRequest;
import com.mealfit.user.controller.dto.response.UserInfoResponse;
import com.mealfit.user.controller.dto.response.UserNutritionGoalResponse;
import java.time.LocalTime;
import org.springframework.web.multipart.MultipartFile;

public class UserFactory {

    private UserFactory() {
    }

    public static User createSaveUser(String username, String password, String nickname,
          String email, UserStatus userStatus) {
        return MockUser.builder()
              .username(username)
              .password(password)
              .nickname(nickname)
              .email(email)
              .providerType(ProviderType.LOCAL)
              .userStatus(userStatus)
              .build();
    }

    public static User createMockLocalUser(Long id, String username, String password,
          String nickname, String email,
          UserStatus userStatus) {
        return MockUser.builder()
              .id(id)
              .username(username)
              .password(password)
              .nickname(nickname)
              .email(email)
              .providerType(ProviderType.LOCAL)
              .userStatus(userStatus)
              .build();
    }

    public static User createMockSocialUser(Long id, String username, String password,
          String nickname, String email,
          ProviderType providerType, UserStatus userStatus) {
        return MockUser.builder()
              .id(id)
              .username(username)
              .password(password)
              .nickname(nickname)
              .email(email)
              .providerType(providerType)
              .userStatus(userStatus)
              .build();
    }

    public static UserSignUpRequest createSignUpRequestDto(String username, String email,
          String password, String passwordCheck,
          String nickname) {
        return UserSignUpRequest.builder()
              .username(username)
              .email(email)
              .password(password)
              .passwordCheck(passwordCheck)
              .nickname(nickname)
              .build();
    }

    public static UserSignUpRequest createSignUpRequestDto(String username, String email,
          String password, String passwordCheck,
          String nickname, double currentWeight, double goalWeight,
          LocalTime startFasting, LocalTime endFasting) {
        return UserSignUpRequest.builder()
              .username(username)
              .email(email)
              .password(password)
              .passwordCheck(passwordCheck)
              .nickname(nickname)
              .currentWeight(currentWeight)
              .goalWeight(goalWeight)
              .startFasting(startFasting)
              .endFasting(endFasting)
              .build();
    }

    public static ChangeUserInfoRequest createChangeUserInfoRequestDto(String nickname,
          MultipartFile profileImage) {
        return ChangeUserInfoRequest.builder()
              .nickname(nickname)
              .build();
    }

    public static ChangeUserInfoRequest createChangeUserInfoRequestDto(String nickname,
          MultipartFile profileImage, double currentWeight, double goalWeight) {
        return ChangeUserInfoRequest.builder()
              .nickname(nickname)
              .profileImage(profileImage)
              .currentWeight(currentWeight)
              .goalWeight(goalWeight)
              .startFasting(LocalTime.now())
              .endFasting(LocalTime.now())
              .build();
    }

    public static ChangeUserInfoRequest createChangeUserInfoRequestDto(String nickname,
          MultipartFile profileImage, double currentWeight, double goalWeight,
          double kcal, double carbs, double protein, double fat) {
        return ChangeUserInfoRequest.builder()
              .nickname(nickname)
              .profileImage(profileImage)
              .currentWeight(currentWeight)
              .goalWeight(goalWeight)
              .startFasting(LocalTime.now())
              .endFasting(LocalTime.now())
              .kcal(kcal)
              .carbs(carbs)
              .protein(protein)
              .fat(fat)
              .build();
    }

    public static UserInfoResponse createMockUserInfoResponseDtoByUserStatus(UserStatus userStatus, ProviderType providerType) {
        return new UserInfoResponse(1L,
              "testUser", "testNickname", "https://github.com/testImage.jpg",
              80.0, userStatus, providerType,
              LocalTime.of(11, 0, 0), LocalTime.of(12, 0, 0)
        );
    }

    public static UserNutritionGoalResponse createUserNutritionGoalResponseDto(double kcal,
          double carbs, double protein, double fat) {
        return new UserNutritionGoalResponse(kcal, carbs, protein, fat);
    }
}
