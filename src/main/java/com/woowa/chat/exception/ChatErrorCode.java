package com.woowa.chat.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ChatErrorCode {
    UNVALID_USER("ChatEx001", "유효하지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED),
    UNVALID_HEADER("ChatEx002", "유효하지 않은 헤더입니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;


}
