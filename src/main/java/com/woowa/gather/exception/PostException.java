package com.woowa.gather.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostException extends RuntimeException {

    private final PostErrorCode errorCode;
}
