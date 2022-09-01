package com.mealfit.diet.presentation.dto.response;

import com.mealfit.diet.domain.Diet;
import com.mealfit.diet.domain.DietStatus;
import com.mealfit.user.domain.User;
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
    private Long foodId;  // 음식 ID
    private double foodWeight; // 음식 중량
    private DietStatus status; // 아침 점심 저녁
    private LocalDate date; // 기록 일자


    public DietCUDResponseDto(Diet diet, User user){
        this.foodId = diet.getFoodId();
        this.foodWeight = diet.getFoodWeight();
        this.status = diet.getStatus();
        this.date = diet.getDietDate();
    }
}
