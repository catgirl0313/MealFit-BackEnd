package com.mealfit.common.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {

    private int status;
    private String code;
    private String message;
    private String detail;

    private ErrorResponse(ErrorCode code) {
        this.status = code.getStatus();
        this.code = code.getCode();
        this.message = code.getMessage();
    }

    private ErrorResponse(ErrorCode code, String detail) {
        this.status = code.getStatus();
        this.code = code.getCode();
        this.message = code.getMessage();
        this.detail = detail;
    }

    public static ErrorResponse of(ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(ErrorCode code, String message) {
        return new ErrorResponse(code, message);
    }
}