package com.mealfit.diet.presentation.dto.response;

import com.mealfit.user.application.dto.response.UserNutritionGoalResponseDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class DietListResponseDto {

    private List<DietResponseDto> dietResponseDto;
    private UserNutritionGoalResponseDto userNutritionGoalResponse;

    public DietListResponseDto(List<DietResponseDto> dietResponseDto,
          UserNutritionGoalResponseDto userNutritionGoalResponse) {
        this.dietResponseDto = dietResponseDto;
        this.userNutritionGoalResponse = userNutritionGoalResponse;
    }
}
