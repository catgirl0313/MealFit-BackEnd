package com.mealfit.exception.authentication;

import com.mealfit.exception.wrapper.ErrorCode;

public class InvalidTokenException extends AuthenticationException {

    private static final ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;

    public InvalidTokenException(String message) {
        super(errorCode, message);
    }
}
