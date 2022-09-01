package com.mealfit.diet.application.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DietListByDateRequestDto {

    private Long userId;
    private LocalDate date;
}
