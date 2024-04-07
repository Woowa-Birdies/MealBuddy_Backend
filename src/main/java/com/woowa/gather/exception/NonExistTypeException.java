package com.woowa.gather.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NonExistTypeException extends RuntimeException {

    private HttpStatus httpStatus;
    private String message;

    public NonExistTypeException(String message) {
        super(message);
        this.httpStatus = HttpStatus.OK;
        this.message = message;
    }
}
