package com.woowa.gather.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AskErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"AskEx001", "사용자를 찾을 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "AskEx002", "게시글을 찾을 수 없습니다."),
    ASK_NOT_FOUND(HttpStatus.NOT_FOUND, "AskEx003", "신청 내역을 찾을 수 없습니다."),
    INVALID_PARAMETER_TYPE(HttpStatus.BAD_REQUEST, "AskEx004", "타입의 범위는 [1,2,3]입니다."),
    PARTICIPATION_DENIED(HttpStatus.BAD_REQUEST, "AskEx005", "이미 모집 마감된 모임입니다."),
    ALREADY_PARTICIPATED_USER(HttpStatus.OK, "AskEx006", "이미 참여한 상태입니다."),
    INVALID_STATUS_VALUE(HttpStatus.BAD_REQUEST, "AskEx007", "수락 또는 거절 상태로만 변경가능합니다"),
    CLOSED_GATHER(HttpStatus.OK, "AskEx008", "모집 마감된 모임입니다."),
    ;

    private HttpStatus httpStatus;
    private String code;
    private String message;
}
