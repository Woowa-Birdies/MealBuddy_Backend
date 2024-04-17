package com.woowa.user.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailVerificationDTO {
	Long userId;
	String token;

	public EmailVerificationDTO(Long userId, String token) {
		this.userId = userId;
		this.token = token;
	}
}
