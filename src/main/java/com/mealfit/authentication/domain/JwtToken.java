package com.mealfit.authentication.domain;

import lombok.Getter;

@Getter
public class JwtToken {

    private final String username;
    private final String token;
    private final long expiredTime;
    private final JwtTokenType jwtTokenType;

    public JwtToken(String username, String token, long expiredTime, JwtTokenType jwtTokenType) {
        this.username = username;
        this.token = token;
        this.expiredTime = expiredTime;
        this.jwtTokenType = jwtTokenType;
    }
}
