package com.woowa.common.domain.dto;

public class ExceptionResult {
	private String code;
	private String message;

	public ExceptionResult(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
