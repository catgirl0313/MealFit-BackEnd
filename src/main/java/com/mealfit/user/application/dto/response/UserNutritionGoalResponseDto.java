package com.mealfit.user.application.dto.response;

import com.mealfit.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNutritionGoalResponseDto {

    private double kcal;
    private double carbs;
    private double protein;
    private double fat;

    public UserNutritionGoalResponseDto(User user) {
        this.kcal = user.getNutrition().getKcal();
        this.carbs = user.getNutrition().getCarbs();
        this.protein = user.getNutrition().getProtein();
        this.fat = user.getNutrition().getFat();
    }

    public UserNutritionGoalResponseDto(double kcal, double carbs, double protein, double fat) {
        this.kcal = kcal;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }
}
