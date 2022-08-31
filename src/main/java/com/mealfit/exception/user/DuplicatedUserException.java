package com.mealfit.exception.user;

import com.mealfit.exception.wrapper.ErrorCode;

public class DuplicatedUserException extends UserException {

    private static final ErrorCode errorCode = ErrorCode.DUPLICATE_SIGNUP_INPUT;

    public DuplicatedUserException(String message) {
        super(errorCode, message);
    }
}
