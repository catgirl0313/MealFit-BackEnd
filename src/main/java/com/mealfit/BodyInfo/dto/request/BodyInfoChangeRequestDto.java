package com.mealfit.BodyInfo.dto.request;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BodyInfoChangeRequestDto implements Serializable {

    @NotNull(message = "ID 값 누락")
    private Long id;

    @NotNull(message = "몸무게를 입력해주세요.")
    private double weight;

    @NotNull(message = "체지방을 입력해주세요.")
    private double bodyFat;
}
