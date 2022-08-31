package com.mealfit.food.domain;

import com.mealfit.common.baseEntity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 영양성분은 100g당 단위임.
@Getter
@Entity
@NoArgsConstructor
@Builder
public class Food extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String foodName; // 음식 이름

    @Column(columnDefinition = "double default 0")
    private double oneServing; // 1회 제공량

    @Column(columnDefinition = "double default 0")
    private double kcal; // 칼로리

    @Column(columnDefinition = "double default 0")
    private double carbs; // 탄수화물

    @Column(columnDefinition = "double default 0")
    private double protein; // 단백질

    @Column(columnDefinition = "double default 0")
    private double fat; // 지방

    private String madeBy;

    public Food(Long id, String foodName, double oneServing, double kcal, double carbs, double protein, double fat, String madeBy) {
        this.id = id;
        this.oneServing = oneServing;
        this.foodName = foodName;
        this.kcal = kcal;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
        this.madeBy = madeBy;
    }
}
