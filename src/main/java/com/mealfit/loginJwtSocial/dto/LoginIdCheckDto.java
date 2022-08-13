package com.mealfit.loginJwtSocial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginIdCheckDto {
    private String username;
    private String nickname;
}