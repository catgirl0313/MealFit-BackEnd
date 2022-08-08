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

    private double calories;

    private double carbs;

    private double protein;

    private double fat;

}
