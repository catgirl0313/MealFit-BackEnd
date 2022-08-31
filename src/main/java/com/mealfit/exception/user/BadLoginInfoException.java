package com.mealfit.exception.user;

import com.mealfit.exception.wrapper.ErrorCode;

public class BadLoginInfoException extends UserException {

    private static final ErrorCode errorCode = ErrorCode.BAD_LOGIN_INFO;

    public BadLoginInfoException(String message) {
        super(errorCode, message);
    }
}
