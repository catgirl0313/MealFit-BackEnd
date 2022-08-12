package com.mealfit.food.controller;


import com.mealfit.food.domain.Food;
import com.mealfit.food.dto.FoodResponseDto;
import com.mealfit.food.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    //식단 조회
    @GetMapping("/diet")
    public ResponseEntity<FoodResponseDto> getFood(@RequestParam("name") Food food){
        return ResponseEntity.status(HttpStatus.OK)
                .body(foodService.getFood(food));

    }

}
