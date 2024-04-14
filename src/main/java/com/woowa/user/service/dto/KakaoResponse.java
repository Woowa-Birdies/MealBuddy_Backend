package com.woowa.user.service.dto;

import static com.woowa.common.domain.SecurityConstant.*;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {
	private final Map<String, Object> attribute;
	private final Map<String, Object> kakaoAccount;
	private final Map<String, Object> kakaoProfile;

	public KakaoResponse(Map<String, Object> attribute) {
		this.attribute = attribute;
		this.kakaoAccount = (Map<String, Object>)attribute.get("kakao_account");
		this.kakaoProfile = (Map<String, Object>)this.kakaoAccount.get("profile");
	}

	@Override
	public String getProvider() {
		return KAKAO;
	}

	@Override
	public String getProviderId() {
		return attribute.get(ID).toString();
	}

	@Override
	public String getName() {
		return kakaoProfile.get(NICKNAME).toString();
	}
}
