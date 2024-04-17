package com.woowa.user.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateProfileRequest {
	@NotNull
	private Long userId;
	@NotEmpty
	private String nickname;
	@NotEmpty
	private String introduce;

	public UpdateProfileRequest(Long userId, String nickname, String introduce) {
		this.userId = userId;
		this.nickname = nickname;
		this.introduce = introduce;
	}
}