package com.woowa.user.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {
	@NotNull
	private Long userId;
	@NotEmpty
	private String nickname;
	@NotNull
	private String verificationHash;
	private String registerNumber;
	@Email
	private String email;

	public SignupRequest(Long userId, String nickname, String verificationHash, String registerNumber, String email) {
		this.userId = userId;
		this.nickname = nickname;
		this.verificationHash = verificationHash;
		this.registerNumber = registerNumber;
		this.email = email;
	}
}
