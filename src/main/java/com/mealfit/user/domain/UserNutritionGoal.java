package com.mealfit.user.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserNutritionGoal {

    @Column(nullable = true)
    private double kcal;

    @Column(nullable = true)
    private double carbs;

    @Column(nullable = true)
    private double protein;

    @Column(nullable = true)
    private double fat;

    public UserNutritionGoal(double kcal, double carbs, double protein, double fat) {
        this.kcal = kcal;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }

    public void updateUserNutritionGoal(double kcal, double carbs, double protein, double fat) {
        this.kcal = kcal;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }
}
