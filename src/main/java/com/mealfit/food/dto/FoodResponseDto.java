package com.mealfit.food.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class FoodResponseDto {
    private String foodName; // 음식 이름

    private double Kcal; // 칼로리

    private double carbs; // 탄수화물

    private double protein; // 단백질

    private double fat; // 지방

    public FoodResponseDto(String foodName, double kcal, double carbs, double protein, double fat) {
        this.foodName = foodName;
        Kcal = kcal;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }
}
