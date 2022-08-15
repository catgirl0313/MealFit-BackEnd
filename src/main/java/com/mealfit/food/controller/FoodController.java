package com.mealfit.food.controller;


import com.mealfit.diet.dto.DietCUDResponseDto;
import com.mealfit.food.domain.Food;
import com.mealfit.food.dto.FoodRequestDto;
import com.mealfit.food.dto.FoodResponseDto;
import com.mealfit.food.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    // 음식 검색
    @GetMapping("/food")
    public ResponseEntity<FoodResponseDto> getFood(@RequestParam("name") String food, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl){
        return ResponseEntity.status(HttpStatus.OK)
                .body(foodService.getFood(food, userDetailsImpl.getUser()));

    }

    // 음식 입력
    @PostMapping("/food")
    public ResponseEntity<String> createFood(@RequestBody FoodRequestDto requestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        foodService.createFood(requestDto, userDetailsImpl.getUser());

        return ResponseEntity.status(HttpStatus.OK)
                .body("음식 입력 완료");
    }

}
