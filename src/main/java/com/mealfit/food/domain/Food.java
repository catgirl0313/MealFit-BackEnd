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

    @Column(nullable = false)
    private double foodWeight; // 1회 제공량

    @Column(nullable = false)
    private double kCal; // 칼로리

    @Column(nullable = false)
    private double carbs; // 탄수화물

    @Column(nullable = false)
    private double protein; // 단백질

    @Column(nullable = false)
    private double fat; // 지방

    public Food(Long id, String foodName, double foodWeight, double kCal, double carbs, double protein, double fat) {
        this.id = id;
        this.foodName = foodName;
        this.foodWeight = foodWeight;
        this.kCal = kCal;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }
}
