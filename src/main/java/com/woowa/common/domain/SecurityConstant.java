package com.woowa.common.domain;

import org.springframework.stereotype.Component;

@Component
public final class SecurityConstant {
	public static final String RESPONSE = "response";
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String SUB = "sub";
	public static final String EMAIL = "email";
	public static final String GOOGLE = "google";
	public static final String NAVER = "naver";
	public static final String KAKAO = "kakao";
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer ";
	public static final String ROLE_USER = "ROLE_USER";
	public static final String PROFILE = "profile";
	public static final String ACCESS_TOKEN = "access";
	public static final String REFRESH_TOKEN = "refresh";
	public static final Long ACCESS_TOKEN_DURATION = 1800000L; // 30분
	public static final Long REFRESH_TOKEN_DURATION = 604800000L; // 7일
}