package com.mealfit.diet.dto;

import com.mealfit.diet.domain.DietStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
public class PostRequestDto { // 업데이트 용으로 다시 써야지
    private Long dietId;  // 식단 ID
    private Long foodId; // 음식 ID,
    private Long changeTo; // 바꿀 음식ID,
    private Long foodWeight; // 음식 중량

    public PostRequestDto(Long dietId, Long foodId, Long changeTo, Long foodWeight) {
        this.dietId = dietId;
        this.foodId = foodId;
        this.changeTo = changeTo;
        this.foodWeight = foodWeight;
    }
}
