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

    public ErrorResponse(ErrorCode code) {
        this.status = code.getStatus();
        this.code = code.getCode();
        this.message = code.getMessage();
        this.detail = code.getDetail();
    }

    public static ErrorResponse of(ErrorCode code) {
        return new ErrorResponse(code);
    }
}