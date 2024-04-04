package com.woowa.user.domain.dto;

import lombok.Getter;

@Getter
public class UserDTO {
	private final String role;
	private final String name;
	private final String userId;
	private final String socialCode;

	public UserDTO(String role, String name, String userId, String socialCode) {
		this.role = role;
		this.name = name;
		this.userId = userId;
		this.socialCode = socialCode;
	}
}
