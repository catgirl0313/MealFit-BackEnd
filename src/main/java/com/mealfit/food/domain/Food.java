package com.mealfit.food.domain;

import com.mealfit.common.baseEntity.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// 영양성분은 100g당 단위임.
@Entity
public class Food extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String foodName; // 음식 이름

    private double calories; // 칼로리

    private double carbs; // 탄수화물

    private double protein; // 단백질

    private double fat; // 지방

}
