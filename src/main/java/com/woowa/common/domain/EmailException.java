package com.woowa.common.domain;

public class EmailException extends RuntimeException {
	public EmailException() {
		super("메일 전송 중 에러가 발생했습니다.");
	}
}
