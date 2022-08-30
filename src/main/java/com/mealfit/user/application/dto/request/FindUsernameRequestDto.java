package com.mealfit.user.application.dto.request;

import lombok.Getter;

@Getter
public class FindUsernameRequestDto {

    private String redirectUrl;
    private String sendingEmail;

    public FindUsernameRequestDto(String redirectUrl, String sendingEmail) {
        this.redirectUrl = redirectUrl;
        this.sendingEmail = sendingEmail;
    }
}
