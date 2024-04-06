package com.woowa.user.domain;

public enum SocialCode {
	GOOGLE, KAKAO, NAVER;

	public static SocialCode toCode(String code) {
		return SocialCode.valueOf(code.toUpperCase());
	}
}
