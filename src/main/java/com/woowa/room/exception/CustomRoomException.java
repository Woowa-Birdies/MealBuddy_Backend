package com.woowa.room.exception;

import lombok.Getter;

@Getter
public class CustomRoomException extends RuntimeException{
    private final RoomErrorCode errorCode;

    public CustomRoomException(RoomErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
