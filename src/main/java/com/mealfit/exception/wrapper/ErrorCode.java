package com.mealfit.exception.wrapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
@JsonFormat(shape = Shape.OBJECT)
public enum ErrorCode implements ErrorModel {

    // User
    DUPLICATE_SIGNUP_INPUT(HttpStatus.BAD_GATEWAY, "U001", "이미 해당 값이 존재합니다."),
    WRONG_SIGNUP_PASSWORD(HttpStatus.BAD_REQUEST, "U002", "잘못된 비밀번호입니다."),

    // COMMON
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C001", "서버 내부 에러"),
    INVALID_CODE(HttpStatus.BAD_REQUEST, "C002", "Invalid Code"),
    RESOURCE_NOT_FOUND(HttpStatus.BAD_REQUEST, "C003", "Resource not found"),
    EXPIRED_CODE(HttpStatus.BAD_REQUEST, "C004", "Expired Code"),

    // AWS
    AWS_ERROR(HttpStatus.BAD_REQUEST, "A001", "aws client error"),

    // javax.validation
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "V001", "잘못된 입력값입니다.");

    private HttpStatus httpStatus;
    private String code;
    private String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.code = code;
    }

    public static ErrorCode of(String code) {
        switch (code) {
            case "Email":
            case "MIN":
            case "NotBlank":
            case "NotNull":
                return ErrorCode.INVALID_INPUT;

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