package com.woowa.user.service.dto;

import static com.woowa.common.domain.SecurityConstant.*;

import java.util.Map;

public class NaverResponse implements OAuth2Response {

	private final Map<String, Object> attribute;

	public NaverResponse(Map<String, Object> attribute) {
		this.attribute = (Map<String, Object>)attribute.get(RESPONSE);
	}

	@Override
	public String getProvider() {
		return NAVER;
	}

	@Override
	public String getProviderId() {
		return attribute.get(ID).toString();
	}

	@Override
	public String getName() {
		return attribute.get(NAME).toString();
	}
}
