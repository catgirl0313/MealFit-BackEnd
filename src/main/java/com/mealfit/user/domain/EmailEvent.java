package com.mealfit.user.domain;


import com.mealfit.user.application.dto.request.SendEmailRequestDto.EmailType;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class EmailEvent {

    private String username;
    private String redirectUrl;
    private String sendingEmail;
    private EmailType emailType;

    public EmailEvent(String username, String redirectUrl, String sendingEmail,
          EmailType emailType) {
        this.username = username;
        this.redirectUrl = redirectUrl;
        this.sendingEmail = sendingEmail;
        this.emailType = emailType;
    }
}
