package com.woowa.user.service.dto;

import static com.woowa.common.domain.SecurityConstant.*;

import java.util.Map;

public class GoogleResponse implements OAuth2Response {

	private final Map<String, Object> attribute;

	public GoogleResponse(Map<String, Object> attribute) {
		this.attribute = attribute;
	}

	@Override
	public String getProvider() {
		return GOOGLE;
	}

	@Override
	public String getProviderId() {
		return attribute.get(SUB).toString();
	}

	@Override
	public String getName() {
		return attribute.get(NAME).toString();
	}
}
