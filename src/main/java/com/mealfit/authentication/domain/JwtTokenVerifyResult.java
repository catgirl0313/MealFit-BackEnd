package com.mealfit.authentication.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenVerifyResult {

    private String username;
    private TokenStatus tokenStatus;

    public enum TokenStatus {
        AVAILABLE,
        EXPIRED,
        DENIED
    }
}
