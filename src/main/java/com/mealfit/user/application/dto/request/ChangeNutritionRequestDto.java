package com.mealfit.user.application.dto.request;

import lombok.Getter;

@Getter
public class ChangeNutritionRequestDto {

    private String username;
    private double kcal;
    private double carbs;
    private double protein;
    private double fat;

    public ChangeNutritionRequestDto(String username, double kcal, double carbs, double protein, double fat) {
        this.kcal = kcal;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }
}
