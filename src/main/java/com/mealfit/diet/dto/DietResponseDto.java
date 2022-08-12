package com.mealfit.diet.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
public class DietResponseDto {
    private List<FoodListDto> foodList;
    private UserGoalDto userGoal;

    public DietResponseDto(List<FoodListDto> foodList, UserGoalDto userGoal) {
        this.foodList = foodList;
        this.userGoal = userGoal;
    }
}
