package com.mealfit.config.security.exception;

import org.springframework.security.core.AuthenticationException;

public class DeniedJwtException extends AuthenticationException {

    public DeniedJwtException(String msg) {
        super(msg);
    }
}
