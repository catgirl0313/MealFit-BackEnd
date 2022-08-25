package com.mealfit.exception.user;

import com.mealfit.exception.wrapper.ErrorCode;

public class DuplicatedSignUpException extends UserException {

    private static final ErrorCode errorCode = ErrorCode.DUPLICATE_SIGNUP_INPUT;

    public DuplicatedSignUpException(String message) {
        super(errorCode, message);
    }
}
