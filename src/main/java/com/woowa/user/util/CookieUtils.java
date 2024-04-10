package com.woowa.user.util;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;

@Component
public class CookieUtils {

	public Cookie createCookie(String key, String value, Long age) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(Math.toIntExact(age));
		cookie.setPath("/");
		cookie.setHttpOnly(true);

		return cookie;
	}
}
