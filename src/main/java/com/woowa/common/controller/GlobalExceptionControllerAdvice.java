package com.woowa.common.controller;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.woowa.common.domain.ResourceNotFoundException;
import com.woowa.common.domain.dto.ExceptionResult;

@ControllerAdvice
public class GlobalExceptionControllerAdvice extends ResponseEntityExceptionHandler {
	/**
	 * exception : Ex000
	 * category : HttpRequestMethodNotSupportedException
	 */
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		return ResponseEntity.status(METHOD_NOT_ALLOWED)
			.body(new ExceptionResult("Ex000", ex.getMessage()));
	}

	/**
	 * exception : Ex001
	 * category : ResourceNotFoundException
	 */
	@ResponseStatus(NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	public ExceptionResult resourceNotFoundException(ResourceNotFoundException exception) {
		return new ExceptionResult("Ex001", exception.getMessage());
	}
}
