package com.mealfit.exception.user;

import com.mealfit.exception.wrapper.ErrorCode;
import com.mealfit.exception.ApplicationException;

public class UserException extends ApplicationException {

    protected UserException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
