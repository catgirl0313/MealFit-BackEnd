package com.mealfit.user.dto.response;

import com.mealfit.user.domain.User;
import javax.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNutritionGoalResponseDto {

    @Min(value = 0, message = "0보다 크게 입력해주세요")
    private double kcal;

    @Min(value = 0, message = "0보다 크게 입력해주세요")
    private double carbs;

    @Min(value = 0, message = "0보다 크게 입력해주세요")
    private double protein;

    @Min(value = 0, message = "0보다 크게 입력해주세요")
    private double fat;

    public UserNutritionGoalResponseDto(User user) {
        this.kcal = user.getUserNutritionGoal().getKcal();
        this.carbs = user.getUserNutritionGoal().getCarbs();
        this.protein = user.getUserNutritionGoal().getProtein();
        this.fat = user.getUserNutritionGoal().getFat();
    }

    public UserNutritionGoalResponseDto(double kcal, double carbs, double protein, double fat) {
        this.kcal = kcal;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }
}
