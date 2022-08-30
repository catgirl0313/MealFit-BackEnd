package com.mealfit.user.presentation.dto;

import com.mealfit.user.presentation.dto.response.UserInfoResponse;
import com.mealfit.user.presentation.dto.response.UserNutritionGoalResponse;
import com.mealfit.user.application.dto.response.UserInfoResponseDto;
import com.mealfit.user.application.dto.response.UserNutritionGoalResponseDto;

public class UserControllerDtoFactory {

    public static UserInfoResponse userInfoResponse(UserInfoResponseDto dto) {
        return UserInfoResponse.builder()
              .userId(dto.getUserId())
              .nickname(dto.getNickname())
              .profileImage(dto.getProfileImage())
              .userStatus(dto.getUserStatus())
              .providerType(dto.getProviderType())
              .goalWeight(dto.getGoalWeight())
              .startFasting(dto.getStartFasting())
              .endFasting(dto.getEndFasting())
              .kcal(dto.getKcal())
              .carbs(dto.getCarbs())
              .protein(dto.getProtein())
              .fat(dto.getFat())
              .build();
    }

    public static UserNutritionGoalResponse userNutritionGoalResponse(
          UserNutritionGoalResponseDto dto) {
        return UserNutritionGoalResponse.builder()
              .kcal(dto.getKcal())
              .carbs(dto.getCarbs())
              .protein(dto.getProtein())
              .fat(dto.getFat())
              .build();
    }
}
