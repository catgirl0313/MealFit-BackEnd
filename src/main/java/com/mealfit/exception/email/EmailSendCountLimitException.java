package com.mealfit.exception.email;

import com.mealfit.exception.wrapper.ErrorCode;

public class EmailSendCountLimitException extends EmailException {

    private static final ErrorCode errorCode = ErrorCode.LIMIT_EMAIL_SEND;

    public EmailSendCountLimitException(String message) {
        super(errorCode, message);
    }
}
