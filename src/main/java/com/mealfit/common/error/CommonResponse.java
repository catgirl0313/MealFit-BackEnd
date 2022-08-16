package com.mealfit.common.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonResponse<T> {

    private int status;
    private String code;
    private String message;
    private String detail;
    private T data;

    private CommonResponse(ErrorCode code) {
        this.status = code.getStatus();
        this.code = code.getCode();
        this.message = code.getMessage();
    }

    private CommonResponse(ErrorCode code, String detail) {
        this.status = code.getStatus();
        this.code = code.getCode();
        this.message = code.getMessage();
        this.detail = detail;
    }

    public CommonResponse(ErrorCode code, T data) {
        this.status = code.getStatus();
        this.code = code.getCode();
        this.message = code.getMessage();
        this.data = data;
    }

    public static CommonResponse<Void> of(ErrorCode code) {
        return new CommonResponse<Void>(code);
    }

    public static CommonResponse<Void> of(ErrorCode code, String message) {
        return new CommonResponse<Void>(code, message);
    }
}