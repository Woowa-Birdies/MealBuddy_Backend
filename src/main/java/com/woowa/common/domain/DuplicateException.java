package com.woowa.common.domain;

public class DuplicateException extends RuntimeException {
	public DuplicateException(Long id, String resource) {
		super(id + "에 해당하는 " + resource + "가 이미 존재합니다.");
	}

	public DuplicateException(String id, String resource) {
		super(id + "에 해당하는 " + resource + "가 이미 존재합니다.");
	}
}
