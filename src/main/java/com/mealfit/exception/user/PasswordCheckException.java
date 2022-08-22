package com.mealfit.exception.user;

import com.mealfit.exception.wrapper.ErrorCode;

public class PasswordCheckException extends UserException {

    private static final ErrorCode errorCode = ErrorCode.WRONG_SIGNUP_PASSWORD;

    public PasswordCheckException(String message) {
        super(errorCode, message);
    }
}
