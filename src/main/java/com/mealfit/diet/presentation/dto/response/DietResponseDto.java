package com.mealfit.diet.presentation.dto.response;

import com.mealfit.diet.domain.DietStatus;
import com.mealfit.food.domain.Food;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DietResponseDto {

    private DietStatus dietStatus;
    private Long dietId;
    private Long foodId;
    private String foodName;
    private double kcal;
    private double carbs;
    private double protein;
    private double fat;
    private double foodWeight;

    public DietResponseDto(DietStatus dietStatus, Long dietId, Food food, double foodWeight) {
        this.dietStatus = dietStatus;
        this.dietId = dietId;
        this.foodId = food.getId();
        this.foodName = food.getFoodName();
        this.kcal = foodWeight*food.getKcal()/food.getOneServing();    // 1회제공량을 나눠서 입력받은 중량만큼 g당 칼로리,탄수화물,단백질을 계산
        this.carbs = foodWeight*food.getCarbs()/food.getOneServing();
        this.protein = foodWeight*food.getProtein()/food.getOneServing();
        this.fat = foodWeight*food.getFat()/food.getOneServing();
        this.foodWeight = foodWeight;
    }

}
