package com.mealfit.user.application.dto.request;

import java.time.LocalTime;
import lombok.Getter;

@Getter
public class ChangeFastingTimeRequestDto {

    private String username;
    private LocalTime startFasting;
    private LocalTime endFasting;

    public ChangeFastingTimeRequestDto(String username, LocalTime startFasting,
          LocalTime endFasting) {
        this.username = username;
        this.startFasting = startFasting;
        this.endFasting = endFasting;
    }
}
