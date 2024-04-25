package com.woowa.common.controller;

import static org.springframework.http.HttpStatus.*;

import com.woowa.chat.exception.ChatErrorCode;
import com.woowa.chat.exception.CustomChatException;
import com.woowa.gather.exception.AskErrorCode;
import com.woowa.gather.exception.AskException;
import com.woowa.gather.exception.PostErrorCode;
import com.woowa.gather.exception.PostException;
import com.woowa.room.exception.CustomRoomException;
import com.woowa.room.exception.RoomErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.woowa.common.domain.DuplicateException;
import com.woowa.common.domain.EmailException;
import com.woowa.common.domain.NotAuthorizedException;
import com.woowa.common.domain.ResourceNotFoundException;
import com.woowa.common.domain.dto.ExceptionResult;
import com.woowa.room.exception.CustomRoomException;
import com.woowa.room.exception.RoomErrorCode;

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
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ExceptionResult> resourceNotFoundException(ResourceNotFoundException exception) {
		return ResponseEntity.status(NOT_FOUND)
			.body(new ExceptionResult("Ex001", exception.getMessage()));
	}

	/**
	 * exception : Ex002
	 * category : NotAuthorizedException
	 */
	@ExceptionHandler(NotAuthorizedException.class)
	public ResponseEntity<ExceptionResult> notAuthorizedException(NotAuthorizedException exception) {
		return ResponseEntity.status(UNAUTHORIZED)
			.body(new ExceptionResult("Ex002", exception.getMessage()));
	}

	/**
	 * exception : Ex003
	 * category : DuplicateException
	 */
	@ExceptionHandler(DuplicateException.class)
	public ResponseEntity<ExceptionResult> duplicateException(DuplicateException exception) {
		return ResponseEntity.status(BAD_REQUEST)
			.body(new ExceptionResult("Ex003", exception.getMessage()));
	}

	/**
	 * exception : Ex004
	 * category : EmailException
	 */
	@ExceptionHandler(EmailException.class)
	public ResponseEntity<ExceptionResult> emailException(EmailException exception) {
		return ResponseEntity.status(BAD_REQUEST)
			.body(new ExceptionResult("Ex004", exception.getMessage()));
	}

	/**
	 * exception : room Domain Exception
	 * category : RoomErrorCode
	 */
	@ExceptionHandler(CustomRoomException.class)
	public ResponseEntity<ExceptionResult> roomException(CustomRoomException exception) {
		final RoomErrorCode errorCode = exception.getErrorCode();
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(new ExceptionResult(errorCode.getCode(), errorCode.getMessage()));
	}

	@ExceptionHandler(AskException.class)
	public ResponseEntity<ExceptionResult> askException(AskException e) {
		final AskErrorCode errorCode = e.getErrorCode();
		return ResponseEntity.status(errorCode.getHttpStatus())
				.body(new ExceptionResult(errorCode.getCode(), errorCode.getMessage()));
	}

	@ExceptionHandler(PostException.class)
	public ResponseEntity<ExceptionResult> postException(PostException e) {
		final PostErrorCode errorCode = e.getErrorCode();
		return ResponseEntity.status(errorCode.getHttpStatus())
				.body(new ExceptionResult(errorCode.getCode(), errorCode.getMessage()));
	}

	@ExceptionHandler(CustomChatException.class)
	public ResponseEntity<ExceptionResult> chatException(CustomChatException exception) {
		final ChatErrorCode errorCode = exception.getErrorCode();
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(new ExceptionResult(errorCode.getCode(), errorCode.getMessage()));
	}
}
