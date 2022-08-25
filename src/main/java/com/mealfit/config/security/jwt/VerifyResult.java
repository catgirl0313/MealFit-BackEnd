package com.mealfit.config.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyResult {

    private String username;
    private TokenStatus tokenStatus;


    public static enum TokenStatus {
        AVAILABLE,
        EXPIRED,
        DENIED
    }
}
