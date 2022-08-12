package com.mealfit.loginJwtSocial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto { //유저가 입력하는값
    private String username;
    private String password;
}
