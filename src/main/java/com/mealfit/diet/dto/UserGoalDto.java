package com.mealfit.diet.dto;

import com.mealfit.user.domain.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserGoalDto {
    private double kcal; // 목표 칼로리
    private double carbs; // 목표 탄수화물
    private double protein; // 목표 단백질
    private double fat; // 목표 지방

    public UserGoalDto(User user) {
        this.kcal = user.getKcal();
        this.carbs = user.getCarbs();
        this.protein = user.getProtein();
        this.fat = user.getFat();
    }
}
