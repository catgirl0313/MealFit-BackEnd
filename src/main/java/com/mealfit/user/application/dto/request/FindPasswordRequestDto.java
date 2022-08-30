package com.mealfit.user.application.dto.request;

import lombok.Getter;

@Getter
public class FindPasswordRequestDto {

    private String username;
    private String redirectUrl;
    private String sendingEmail;

    public FindPasswordRequestDto(String username, String redirectUrl, String sendingEmail) {
        this.username = username;
        this.redirectUrl = redirectUrl;
        this.sendingEmail = sendingEmail;
    }
}
