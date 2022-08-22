package com.mealfit.exception;

import com.mealfit.exception.wrapper.ErrorCode;

/**
 * {@code ApplicationException}는 <em>애플리케이션 실행동안 발생하는
 * 예외를 공통적으로 묶어주는 추상클래스</em>입니다.
 *
 * <p>필드로 <em>errorCode</em>를 가지고 있으며 errorCode의 경우
 * 프로젝트 팀 내부에서 자체적으로 규약을 정해서 사용하면 됩니다.</p>
 *
 * 익셉션의 작동 방식에 대해서는 {@Code RuntimeException}를 참고하십시오.
 *
 * @Since 1.0
 * @Author 9JaHyun
 */
public abstract class ApplicationException extends RuntimeException {

    private final ErrorCode errorCode;

    protected ApplicationException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}