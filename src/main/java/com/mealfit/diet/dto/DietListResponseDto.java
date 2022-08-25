package com.mealfit.diet.dto;

import com.mealfit.user.service.dto.response.UserNutritionGoalResponseDto;
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
