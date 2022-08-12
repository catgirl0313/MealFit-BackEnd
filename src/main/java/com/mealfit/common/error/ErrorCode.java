package com.mealfit.common.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@JsonFormat(shape = Shape.OBJECT)
public enum ErrorCode implements ErrorModel {

    // COMMON
    INVALID_CODE(400, "C001", "Invalid Code"),
    RESOURCE_NOT_FOUND(204, "C002", "Resource not found"),
    EXPIRED_CODE(400, "C003", "Expired Code"),

    // AWS
    AWS_ERROR(400, "A001", "aws client error"),

    // javax.validation
    NOT_NULL(400, "V001", "필수값이 비었습니다."),
    NOT_BLANK(400, "V001", "필수 인풋값이 비었습니다.");

    private int status;
    private String code;
    private String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public static ErrorCode of(String code) {
        switch (code){
            case "NotNull":
                return ErrorCode.NOT_NULL;
            case "NotBlank":
                return ErrorCode.NOT_BLANK;
            default:
                throw new IllegalArgumentException("지원하지 않는 코드입니다.");
        }
    }

    @Override
    public String getKey() {
        return this.code;
    }

    @Override
    public String getValue() {
        return this.message;
    }
}