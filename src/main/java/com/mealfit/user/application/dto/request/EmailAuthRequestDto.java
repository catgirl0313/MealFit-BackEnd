package com.mealfit.user.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailAuthRequestDto {

    private String username;
    private String authKey;

    @Builder
    private EmailAuthRequestDto(String username, String authKey) {
        this.username = username;
        this.authKey = authKey;
    }
}
