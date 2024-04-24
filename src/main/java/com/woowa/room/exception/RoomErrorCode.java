package com.woowa.room.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum RoomErrorCode {

    USER_NOT_FOUND("RoomEx001", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    POST_NOT_FOUND("RoomEx002", "게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ROOM_NOT_FOUND("RoomEx003", "채팅방을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    UNABLE_TO_JOIN("RoomEx004", "채팅방에 참여할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ALREADY_JOINED_ROOM("RoomEx005", "이미 채팅방에 참여하고 있습니다.", HttpStatus.BAD_REQUEST),
    NOT_JOINED_ROOM("RoomEx006", "채팅방에 참여하지 않았습니다.", HttpStatus.BAD_REQUEST),
    UNABLE_TO_KICK("RoomEx007", "강퇴할 수 없습니다.", HttpStatus.FORBIDDEN),
    ROOM_SAVE_FAILED("RoomEx008", "이미 생성된 채팅방이 있습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
