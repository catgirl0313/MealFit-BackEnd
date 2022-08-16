package com.mealfit.diet.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
public class DietListResponseDto {

    private List<DietResponseDto> dietResponseDto;
    private UserGoalDto userGoal;

    public DietListResponseDto(List<DietResponseDto> dietResponseDto, UserGoalDto userGoal) {
        this.dietResponseDto = dietResponseDto;
        this.userGoal = userGoal;
    }
}
