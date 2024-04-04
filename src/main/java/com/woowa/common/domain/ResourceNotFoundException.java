package com.woowa.common.domain;

public class ResourceNotFoundException extends RuntimeException {
	public ResourceNotFoundException(Long id, String resource) {
		super("현재 해당하는 " + id + "에 " + resource + "가 존재하지 않습니다.");
	}

	public ResourceNotFoundException(String id, String resource) {
		super("현재 해당하는 " + id + "에 " + resource + "가 존재하지 않습니다.");
	}
}
