package com.mealfit.food.application.dto;

import com.mealfit.food.presentation.dto.request.CreateFoodRequest;
import com.mealfit.food.application.dto.request.CreateFoodRequestDto;

public class FoodServiceDtoFactory {

    public static CreateFoodRequestDto createFoodRequestDto(CreateFoodRequest request, Long userId) {
        return new CreateFoodRequestDto(
              request.getFoodName(),
              request.getOneServing(),
              request.getKcal(),
              request.getCarbs(),
              request.getProtein(),
              request.getFat(),
              request.getMadeBy());
    }
}
