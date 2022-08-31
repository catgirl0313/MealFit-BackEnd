package com.mealfit.diet.application.dto.response;

import com.mealfit.diet.domain.Diet;
import com.mealfit.diet.domain.DietStatus;
import com.mealfit.food.domain.Food;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DietResponseDto {

    private Long dietId;
    private Long foodId;
    private String foodName;
    private String madeBy;
    private DietStatus dietStatus;
    private double kcal;
    private double carbs;
    private double protein;
    private double fat;
    private double foodWeight;

    public DietResponseDto(Diet diet, Food food) {
        this.dietId = diet.getId();
        this.dietStatus = diet.getStatus();
        this.foodId = food.getId();
        this.madeBy = food.getMadeBy();
        this.foodName = food.getFoodName();
        this.foodWeight = diet.getFoodWeight();
        this.kcal = foodWeight * food.getKcal()
              / food.getOneServing();    // 1회제공량을 나눠서 입력받은 중량만큼 g당 칼로리,탄수화물,단백질을 계산
        this.carbs = foodWeight * food.getCarbs() / food.getOneServing();
        this.protein = foodWeight * food.getProtein() / food.getOneServing();
        this.fat = foodWeight * food.getFat() / food.getOneServing();

    }

}
