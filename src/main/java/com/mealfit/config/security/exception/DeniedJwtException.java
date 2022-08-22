package com.mealfit.config.security.exception;

public class DeniedJwtException extends RuntimeException {

    public DeniedJwtException(String msg) {
        super(msg);
    }
}
