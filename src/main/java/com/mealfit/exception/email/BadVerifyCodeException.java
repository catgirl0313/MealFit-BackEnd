package com.mealfit.exception.email;

import com.mealfit.exception.wrapper.ErrorCode;

public class BadVerifyCodeException extends EmailException{

    private static final ErrorCode errorCode = ErrorCode.BAD_VERIFY_CODE;

    public BadVerifyCodeException(String message) {
        super(errorCode, message);
    }
}
