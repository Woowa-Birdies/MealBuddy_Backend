package com.woowa.common.domain;

public class NotAuthorizedException extends RuntimeException {
	public NotAuthorizedException(String message) {
		super(message);
	}
}
