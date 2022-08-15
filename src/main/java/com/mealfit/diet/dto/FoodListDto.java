package com.mealfit.diet.dto;

import com.mealfit.diet.domain.DietStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class FoodListDto {
    private DietStatus status; // 아침, 점심, 저녁
    private String food; // 음식
    private double kcal; // 칼로리
    private double carbs; // 탄수화물
    private double protein; // 단백질
    private double fat; // 지방

    public FoodListDto(DietStatus status, String food, double kcal, double carbs, double protein, double fat) {
        this.status = status;
        this.food = food;
        this.kcal = kcal;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }
}
