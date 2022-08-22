package com.mealfit.user.domain;

import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class UserNutritionGoal {

    private double kcal;

    private double carbs;

    private double protein;

    private double fat;

    protected UserNutritionGoal() {
    }

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
