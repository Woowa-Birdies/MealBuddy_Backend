package com.woowa.gather.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"PostEx001", "사용자를 찾을 수 없습니다."),
    USER_NOT_VERIFICATION(HttpStatus.NOT_FOUND,"PostEx002", "사용자가 추가 인증을 하지 않았습니다."),
    ;

    private HttpStatus httpStatus;
    private String code;
    private String message;
}
