package com.mealfit.user.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChangeUserPasswordRequestDto {

    private String username;
    private String password;
    private String changePassword;
    private String checkPassword;

    @Builder
    public ChangeUserPasswordRequestDto(String username, String password, String changePassword,
          String checkPassword) {
        this.username = username;
        this.password = password;
        this.changePassword = changePassword;
        this.checkPassword = checkPassword;
    }
}
