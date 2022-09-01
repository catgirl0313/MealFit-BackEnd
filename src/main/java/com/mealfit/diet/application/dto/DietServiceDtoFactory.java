package com.mealfit.diet.application.dto;

import com.mealfit.diet.application.dto.request.DietChangeRequestDto;
import com.mealfit.diet.application.dto.request.DietCreateRequestDto;
import com.mealfit.diet.application.dto.request.DietListByDateRequestDto;
import com.mealfit.diet.domain.DietStatus;
import com.mealfit.diet.presentation.dto.request.DietChangeRequest;
import com.mealfit.diet.presentation.dto.request.DietCreateRequest;
import java.time.LocalDate;

public class DietServiceDtoFactory {

    public static DietCreateRequestDto dietCreateRequestDto(Long userId, DietCreateRequest request) {
        return new DietCreateRequestDto(userId,
              request.getFoodId(),
              request.getFoodWeight(),
              DietStatus.valueOf(request.getStatus()),
              request.getDate());
    }

    public static DietListByDateRequestDto dietListByDateRequestDto(Long userId,
          LocalDate localDate) {
        return new DietListByDateRequestDto(userId, localDate);
    }

    public static DietChangeRequestDto dietChangeRequestDto(Long userId, DietChangeRequest request) {
        return new DietChangeRequestDto(userId,
              request.getDietId(),
              request.getFoodId(),
              request.getFoodWeight());
    }
}
