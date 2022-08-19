package com.mealfit.food.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class FoodRequestDto {   // 회원이 음식을 직접 입력

    private String foodName; // 음식 이름

    private double foodWeight; // 음식 중량

    private double kCal; // 칼로리

    private double carbs; // 탄수화물

    private double protein; // 단백질

    private double fat; // 지방

    public FoodRequestDto(String foodName, double foodWeight, double kCal, double carbs, double protein, double fat) {
        this.foodName = foodName;
        this.foodWeight = foodWeight;
        this.kCal = kCal;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }
}
