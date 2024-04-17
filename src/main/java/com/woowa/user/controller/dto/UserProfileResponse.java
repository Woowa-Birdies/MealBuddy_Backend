package com.woowa.user.controller.dto;

import com.woowa.user.domain.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfileResponse {
	private Long userId;
	private String nickname;
	private String introduce;

	public UserProfileResponse(Long userId, String nickname, String introduce) {
		this.userId = userId;
		this.nickname = nickname;
		this.introduce = introduce;
	}

	public static UserProfileResponse toResponse(User user) {
		return new UserProfileResponse(user.getId(), user.getNickname(), user.getIntroduce());
	}
}
