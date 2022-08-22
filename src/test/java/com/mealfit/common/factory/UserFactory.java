package com.mealfit.common.factory;

import com.mealfit.user.domain.ProviderType;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserStatus;
import com.mealfit.user.dto.request.SignUpRequestDto;
import com.mealfit.user.dto.response.UserInfoResponseDto;
import com.mealfit.user.dto.response.UserNutritionGoalResponseDto;
import java.time.LocalTime;

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

    public static SignUpRequestDto createSignUpRequestDto(String username, String email,
          String password, String passwordCheck,
          String nickname) {
        return SignUpRequestDto.builder()
              .username(username)
              .email(email)
              .password(password)
              .passwordCheck(passwordCheck)
              .nickname(nickname)
              .build();
    }

    public static SignUpRequestDto createSignUpRequestDto(String username, String email,
          String password, String passwordCheck,
          String nickname, double currentWeight, double goalWeight,
          LocalTime startFasting, LocalTime endFasting) {
        return SignUpRequestDto.builder()
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

    public static UserInfoResponseDto createMockUserInfoResponseDtoByUserStatus(UserStatus userStatus) {
        return new UserInfoResponseDto(
              "testUser", "testNickname", "https://github.com/testImage.jpg",
              80.0, userStatus,
              LocalTime.of(11, 0, 0), LocalTime.of(12, 0, 0)
        );
    }

    public static UserNutritionGoalResponseDto createUserNutritionGoalResponseDto(double kcal,
          double carbs, double protein, double fat) {
        return new UserNutritionGoalResponseDto(kcal, carbs, protein, fat);
    }
}
