package com.mealfit.exception.user;

import com.mealfit.exception.wrapper.ErrorCode;

public class NoUserException extends UserException {

    private static final ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;

    public NoUserException(String message) {
        super(errorCode, message);
    }
}