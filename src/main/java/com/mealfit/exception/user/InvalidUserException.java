package com.mealfit.exception.user;

import com.mealfit.exception.wrapper.ErrorCode;

public class InvalidUserException extends UserException {

    private static final ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;

    public InvalidUserException(String message) {
        super(errorCode, message);
    }
}