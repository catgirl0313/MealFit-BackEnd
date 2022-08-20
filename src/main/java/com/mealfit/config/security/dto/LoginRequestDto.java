package com.mealfit.config.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto implements Serializable { //유저가 입력하는값
    private String username;
    private String password;
}
