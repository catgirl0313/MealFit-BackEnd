package com.mealfit.food.application.dto.response;

import com.mealfit.food.domain.Food;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FoodInfoResponseDto {

    private Long foodId;
    private String foodName;
    private double oneServing;
    private double kcal;
    private double carbs;
    private double protein;
    private double fat;
    private String madeBy;

    public FoodInfoResponseDto(Food food) {
        this.foodId = food.getId();
        this.foodName = food.getFoodName();
        this.oneServing = food.getOneServing();
        this.kcal = food.getKcal();
        this.carbs = food.getCarbs();
        this.protein = food.getProtein();
        this.fat = food.getFat();
        this.madeBy = food.getMadeBy();
    }
}
