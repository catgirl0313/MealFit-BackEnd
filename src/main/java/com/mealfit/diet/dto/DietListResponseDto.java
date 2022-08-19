package com.mealfit.diet.dto;

import com.mealfit.user.dto.UserInfoChangeRequestDto.UserNutritionGoalDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class DietListResponseDto {

    private List<DietResponseDto> dietResponseDto;
    private UserNutritionGoalDto userNutritionGoalDto;

    public DietListResponseDto(List<DietResponseDto> dietResponseDto,
          UserNutritionGoalDto userNutritionGoalDto) {
        this.dietResponseDto = dietResponseDto;
        this.userNutritionGoalDto = userNutritionGoalDto;
    }
}
