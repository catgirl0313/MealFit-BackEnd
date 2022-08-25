package com.mealfit.diet.dto;

import com.mealfit.user.dto.response.UserNutritionGoalResponseDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class DietListResponseDto {

    private List<DietResponseDto> dietResponseDto;
    private UserNutritionGoalResponseDto userNutritionGoalResponseDto;

    public DietListResponseDto(List<DietResponseDto> dietResponseDto,
          UserNutritionGoalResponseDto userNutritionGoalResponseDto) {
        this.dietResponseDto = dietResponseDto;
        this.userNutritionGoalResponseDto = userNutritionGoalResponseDto;
    }
}
