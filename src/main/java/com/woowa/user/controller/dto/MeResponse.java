package com.woowa.user.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MeResponse {
	private Long userId;

	public MeResponse(Long userId) {
		this.userId = userId;
	}
}
