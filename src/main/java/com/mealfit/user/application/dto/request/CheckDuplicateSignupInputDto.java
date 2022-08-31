package com.mealfit.user.application.dto.request;

import lombok.Getter;

@Getter
public class CheckDuplicateSignupInputDto {

    private String key;
    private String value;

    public CheckDuplicateSignupInputDto(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
