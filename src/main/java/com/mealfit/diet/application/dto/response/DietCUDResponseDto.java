package com.mealfit.diet.application.dto.response;

import com.mealfit.diet.domain.Diet;
import com.mealfit.diet.domain.DietStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DietCUDResponseDto {

    private Long dietId;
    private Long foodId;  // 음식 ID
    private double foodWeight; // 음식 중량
    private Long userId;
    private DietStatus status; // 아침 점심 저녁
    private LocalDate date; // 기록 일자

    public DietCUDResponseDto(Diet diet) {
        this.dietId = diet.getId();
        this.foodId = diet.getFoodId();
        this.foodWeight = diet.getFoodWeight();
        this.userId = diet.getUserId();
        this.status = diet.getStatus();
        this.date = diet.getDietDate();
    }
}
