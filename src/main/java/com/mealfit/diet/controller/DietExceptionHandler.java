package com.mealfit.diet.controller;

import com.mealfit.common.error.ErrorCode;
import com.mealfit.common.error.CommonResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.mealfit.diet.controller")
public class DietExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<CommonResponse<Void>> exception(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonResponse.of(ErrorCode.INVALID_CODE, exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<CommonResponse<Void>> exception(IllegalStateException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonResponse.of(ErrorCode.INVALID_CODE, exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Void>> methodValidException(
          MethodArgumentNotValidException exception,
          HttpServletRequest request) {
        log.warn("MethodArgumentNotValidException 발생!!! url:{}, trace:{}", request.getRequestURI(),
              exception.getStackTrace());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(makeErrorResponse(exception.getBindingResult()));
    }

    private CommonResponse<Void> makeErrorResponse(BindingResult bindingResult){
        //에러가 있다면
        if(bindingResult.hasErrors()){
            String detail = bindingResult.getFieldError().getDefaultMessage();

            String bindResultCode = bindingResult.getFieldError().getCode();

            ErrorCode errorCode = ErrorCode.of(bindResultCode);
            return CommonResponse.of(errorCode, detail);
        }

        return CommonResponse.of(ErrorCode.INVALID_CODE);
    }
}
