package com.woowa.chat.exception;

import lombok.Getter;

@Getter
public class CustomChatException extends RuntimeException{
    private final ChatErrorCode errorCode;

    public CustomChatException(ChatErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
