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
    PARTICIPATION_DENIED(HttpStatus.BAD_REQUEST, "AskEx005", "이미 모집 마감된 모임 입니다."),
    UNVERIFIED_USER(HttpStatus.BAD_REQUEST, "AskEx006", "검증이 필요한 사용자 입니다."),
    ALREADY_ASKED_USER(HttpStatus.BAD_REQUEST, "AskEx007", "이미 신청한 상태 입니다"),
    ALREADY_PARTICIPATED_USER(HttpStatus.BAD_REQUEST, "AskEx008", "이미 참여한 상태 입니다."),
    INVALID_STATUS_VALUE(HttpStatus.BAD_REQUEST, "AskEx009", "수락 또는 거절 상태로만 변경가능 합니다"),
    CLOSED_GATHER(HttpStatus.OK, "AskEx010", "종료된 모임입니다."),
    ASK_DENIED(HttpStatus.OK, "AskEx011", "모집중인 모임이 아닙니다."),
    ;

    private HttpStatus httpStatus;
    private String code;
    private String message;
}
