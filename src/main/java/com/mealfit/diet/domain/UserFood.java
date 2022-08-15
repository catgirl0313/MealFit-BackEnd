package com.mealfit.diet.domain;

import com.mealfit.food.domain.Food;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class UserFood {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private Food userFood; // 유저가 입력한 음식 저장

    @Column(nullable = false)
    private Long userid;

    public UserFood(Food userFood, Long userid){
        this.userFood = userFood;
        this.userid = userid;
    }
}
