package com.woowa.gather.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AskErrorCode {
    INVALID_PARAMETER_TYPE(HttpStatus.BAD_REQUEST, "AskEx001", "타입의 범위는 [1,2,3]입니다."),
    PARTICIPATION_DENIED(HttpStatus.BAD_REQUEST, "AskEx002", "이미 모집 마감된 모임입니다."),
    ALREADY_PARTICIPATED_USER(HttpStatus.OK, "AskEx003", "이미 참여한 상태입니다."),
    ;

    private HttpStatus httpStatus;
    private String code;
    private String message;
}
