package com.mealfit.user.controller;

import com.mealfit.common.error.ErrorCode;
import com.mealfit.common.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.mealfit.user.controller")
public class UserExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exception(
          IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(ErrorResponse.of(ErrorCode.INVALID_CODE, exception.getMessage()));
    }
}
