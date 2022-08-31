package com.mealfit.user.presentation.dto.request;

import java.io.Serializable;
import java.time.LocalTime;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class ChangeFastingTimeRequest implements Serializable {

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startFasting;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endFasting;
}
