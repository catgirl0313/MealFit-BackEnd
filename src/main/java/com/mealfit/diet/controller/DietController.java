package com.mealfit.diet.controller;

import com.mealfit.diet.dto.DietCUDResponseDto;
import com.mealfit.diet.dto.DietListResponseDto;
import com.mealfit.diet.dto.DietRequestDto;
import com.mealfit.diet.dto.PostRequestDto;
import com.mealfit.diet.service.DietService;
import com.mealfit.loginJwtSocial.auth.UserDetailsImpl;
import com.mealfit.user.domain.User;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;

    //식단 조회
    @GetMapping("/diet")
    public ResponseEntity<DietListResponseDto> getDiet(
          @RequestParam("date")
          @DateTimeFormat(pattern = "yyyy-MM-dd")
          LocalDate date,
          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return ResponseEntity.status(HttpStatus.OK)
              .body(dietService.getDiet(date, userDetailsImpl.getUser()));

    }

    //식단 입력
    @PostMapping("/diet")
    public ResponseEntity<DietCUDResponseDto> createDiet(@RequestBody DietRequestDto requestDto,
          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        DietCUDResponseDto responseDto = dietService.createDiet(requestDto,
              userDetailsImpl.getUser());

        return ResponseEntity.status(HttpStatus.CREATED)
              .body(responseDto);
    }

    //식단 수정
    @PutMapping("/diet")
    public ResponseEntity<Void> updateDiet(@RequestBody PostRequestDto requestDto,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        dietService.updateDiet(requestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK)
              .body(null);
    }

    //식단 삭제
    @DeleteMapping("/diet")
    public Long deleteDiet(Long dietId, @AuthenticationPrincipal User user) {
        return dietService.deleteDiet(dietId, user);
    }


}
