package com.mealfit.diet.dto;

import com.mealfit.user.domain.UserNutritionGoal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserGoalDto {
    private double kcal; // 목표 칼로리
    private double carbs; // 목표 탄수화물
    private double protein; // 목표 단백질
    private double fat; // 목표 지방

    public UserGoalDto(UserNutritionGoal userNutritionGoal) {
        this.kcal = userNutritionGoal.getKcal();
        this.carbs = userNutritionGoal.getCarbs();
        this.protein = userNutritionGoal.getProtein();
        this.fat = userNutritionGoal.getFat();
    }
}
