package com.mealfit.user.application.dto.request;

import lombok.Getter;

@Getter
public class SendEmailRequestDto {

    private String username;
    private String redirectUrl;
    private String sendingEmail;
    private EmailType emailType;

    public SendEmailRequestDto(String username, String redirectUrl, String sendingEmail,
          EmailType emailType) {
        this.username = username;
        this.redirectUrl = redirectUrl;
        this.sendingEmail = sendingEmail;
        this.emailType = emailType;
    }

    public enum EmailType {
        VALID_NEW_ACCOUNT,
        FIND_USERNAME,
        FIND_PASSWORD
    }

}
