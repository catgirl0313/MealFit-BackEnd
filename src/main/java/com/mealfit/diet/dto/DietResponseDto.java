package com.mealfit.diet.dto;

import com.mealfit.diet.domain.DietStatus;
import com.mealfit.food.domain.Food;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DietResponseDto {

    private DietStatus dietStatus;
    private Long foodId;
    private String foodName;
    private double kcal;
    private double carbs;
    private double protein;
    private double fat;
    private double foodWeight;

    public DietResponseDto(DietStatus dietStatus, Food food, double foodWeight) {
        this.dietStatus = dietStatus;
        this.foodId = food.getId();
        this.foodName = food.getFoodName();
        this.kCal = food.getKCal();
        this.carbs = food.getCarbs();
        this.protein = food.getProtein();
        this.fat = food.getFat();
        this.foodWeight = foodWeight;
    }

}
