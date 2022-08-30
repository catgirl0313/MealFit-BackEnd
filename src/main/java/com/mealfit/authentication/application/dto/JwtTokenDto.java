package com.mealfit.authentication.application.dto;

import lombok.Getter;

@Getter
public class JwtTokenDto {

    private String username;
    private String token;

    public JwtTokenDto(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
