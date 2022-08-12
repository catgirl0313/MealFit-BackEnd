package com.mealfit.diet.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class UserGoalDto {
    private double kcal; // 목표 칼로리
    private double carbs; // 목표 탄수화물
    private double protein; // 목표 단백질
    private double fat; // 목표 지방

    public UserGoalDto(double kcal, double carbs, double protein, double fat) {
        this.kcal = kcal;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }
}
