package com.mealfit.common;

import com.mealfit.exception.wrapper.ErrorCode;
import com.mealfit.exception.wrapper.ErrorResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class CommonDocController {

    @PostMapping("/error")
    public void errorSample(@RequestBody @Valid SampleRequest dto) {
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SampleRequest {

        @NotEmpty
        private String name;

        @Email
        private String email;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodValidException(
          MethodArgumentNotValidException exception,
          HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(makeErrorResponse(exception.getBindingResult()));
    }

    private ErrorResponse makeErrorResponse(BindingResult bindingResult){
        String detail = "";

        //에러가 있다면
        if(bindingResult.hasErrors()){
            detail = bindingResult.getFieldError().getDefaultMessage();

            String bindResultCode = bindingResult.getFieldError().getCode();

            ErrorCode errorCode = ErrorCode.of(bindResultCode);
            return ErrorResponse.of(errorCode, detail);
        }

        return ErrorResponse.of(ErrorCode.INVALID_CODE, detail);
    }
}