package com.mealfit.exception.authentication;

import org.springframework.security.core.AuthenticationException;

public class BadLoginInfoException extends AuthenticationException {

    public BadLoginInfoException(String msg) {
        super(msg);
    }

    public BadLoginInfoException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
