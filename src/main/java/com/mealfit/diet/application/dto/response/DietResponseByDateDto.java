package com.mealfit.diet.application.dto.response;

import com.mealfit.user.application.dto.response.UserNutritionGoalResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DietResponseByDateDto {

    private List<DietResponseDto> dietResponseDto;
    private TodayNutrition todayNutrition;
    private UserNutritionGoalResponseDto userNutritionGoalResponse;

    public DietResponseByDateDto(List<DietResponseDto> dietResponseDto,
          UserNutritionGoalResponseDto userNutritionGoalResponse) {
        this.dietResponseDto = dietResponseDto;
        double kcal = 0;
        double carbs = 0;
        double protein = 0;
        double fat = 0;

        for (DietResponseDto dto : dietResponseDto) {
            kcal += dto.getKcal();
            carbs += dto.getCarbs();
            protein += dto.getProtein();
            fat += dto.getFat();
        }

        this.todayNutrition = new TodayNutrition(kcal, carbs, protein, fat);
              this.userNutritionGoalResponse = userNutritionGoalResponse;
    }

    @Data
    @AllArgsConstructor
    class TodayNutrition {

        private double kcal;
        private double carbs;
        private double protein;
        private double fat;
    }

}
