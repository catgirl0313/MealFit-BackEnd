package com.mealfit.food.dto;

import com.mealfit.food.domain.Food;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FoodResponseDto {
    private Long foodId; // 음식 ID
    private String foodName; // 음식 이름

    private double kCal; // 칼로리

    private double carbs; // 탄수화물

    private double protein; // 단백질

    private double fat; // 지방



    public FoodResponseDto(Food food) {
        this.foodId = food.getId();
        this.foodName = food.getFoodName();
        this.kCal = food.getKCal();
        this.carbs = food.getCarbs();
        this.protein = food.getProtein();
        this.fat = food.getFat();
    }
}
