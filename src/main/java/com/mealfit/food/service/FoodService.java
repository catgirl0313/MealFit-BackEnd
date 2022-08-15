package com.mealfit.food.service;

import com.mealfit.food.domain.Food;
import com.mealfit.food.dto.FoodRequestDto;
import com.mealfit.food.dto.FoodResponseDto;
import com.mealfit.food.repository.FoodRepository;
import com.mealfit.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FoodService {

    private final FoodRepository foodRepository;
    public FoodResponseDto getFood(String food, User user) {
        List<Food> searchFood = foodRepository.findByFoodName(food);

        return FoodResponseDto.builder()  // 여러개니까 리스트로..!
                .foodName(searchFood.getFoodName())
                .Kcal(searchFood.getKcal())
                .carbs(searchFood.getCarbs())
                .protein(searchFood.getProtein())
                .fat(searchFood.getFat())
                .build();
    }

    public void createFood(FoodRequestDto requestDto, User user) {



    }
}
