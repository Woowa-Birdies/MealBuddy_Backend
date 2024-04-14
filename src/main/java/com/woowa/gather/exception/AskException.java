package com.woowa.gather.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AskException extends RuntimeException{

    private final AskErrorCode errorCode;
}
