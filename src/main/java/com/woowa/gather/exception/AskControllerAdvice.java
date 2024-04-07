package com.woowa.gather.exception;

import com.woowa.common.domain.dto.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AskControllerAdvice {

    /**
     * code : AskEx001
     * category : NonExistTypeException
     * 정해진 범위의 param 또는 path variable 값 외의 값이 전달될 때 발생
     * ex) type 이라는 param 에 [0, 1, 2] 만 보낼 수 있는데 5를 보내면 발생
     */
    @ExceptionHandler(NonExistTypeException.class)
    public ResponseEntity<ExceptionResult> nonExistTypeException(NonExistTypeException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(new ExceptionResult("AskEx001", e.getMessage()));
    }
}
