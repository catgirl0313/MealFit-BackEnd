package com.mealfit.diet.dto;

import com.mealfit.diet.domain.Diet;
import com.mealfit.diet.domain.DietStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DietRequestDto {
    private Long foodId;  // 음식 ID
    private Long foodWeight; // 음식 중량
    private String status; // 아침 점심 저녁
    private LocalDate date; // 기록 일자

    public Diet toEntity() {
        return new Diet(foodId, DietStatus.valueOf(status), foodWeight, date);
    }
}
