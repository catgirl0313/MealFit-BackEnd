package com.mealfit.exception.email;

import com.mealfit.exception.ApplicationException;
import com.mealfit.exception.wrapper.ErrorCode;

public class EmailException extends ApplicationException {

    protected EmailException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
