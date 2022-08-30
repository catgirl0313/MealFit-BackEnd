package com.mealfit.user.domain;

import java.time.LocalTime;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class FastingTime {

    private LocalTime startFasting;

    private LocalTime endFasting;

    public FastingTime(LocalTime startFasting, LocalTime endFasting) {
        this.startFasting = startFasting;
        this.endFasting = endFasting;
    }
}
