package com.mealfit.exception.authentication;

import com.mealfit.exception.ApplicationException;
import com.mealfit.exception.wrapper.ErrorCode;

public class AuthenticationException extends ApplicationException {

    protected AuthenticationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
