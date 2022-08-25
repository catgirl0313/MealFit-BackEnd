package com.mealfit.food.service;

import com.mealfit.food.domain.Food;
import com.mealfit.food.dto.FoodRequestDto;
import com.mealfit.food.dto.FoodResponseDto;
import com.mealfit.food.repository.FoodRepository;
import com.mealfit.user.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FoodService {
    private final FoodRepository foodRepository;

    // 음식 검색
    // @Transactional
    public List<FoodResponseDto> getFood(String foodName, User user) {
        List<Food> searchFood = foodRepository.findByFoodName(foodName);

        return searchFood.stream()
              // 람다식 -> 메서드 참조 (method Reference)
              .map(FoodResponseDto::new)        //(타입 -> 다른 타입으로 변경)
              .collect(Collectors.toList());    // List에 담아주기
    }

    // 음식 입력
    @Transactional
    public void createFood(FoodRequestDto requestDto, User user) {

        Food food = Food.builder()
                .foodName(requestDto.getFoodName())
                .oneServing(requestDto.getOneServing())
                .kcal(requestDto.getKcal())
                .carbs(requestDto.getCarbs())
                .protein(requestDto.getProtein())
                .fat(requestDto.getFat())
                .build();
        foodRepository.save(food);
    }
}
