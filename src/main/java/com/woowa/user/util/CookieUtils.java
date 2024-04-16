package com.woowa.user.util;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;

@Component
public class CookieUtils {

	public Cookie createHttpOnlyCookie(String key, String value, Long age) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(Math.toIntExact(age));
		cookie.setPath("/");
		cookie.setSecure(true);
		cookie.setAttribute("SameSite", "None");
		return cookie;
	}

	public Cookie createCookie(String key, String value, Long age) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(Math.toIntExact(age));
		cookie.setPath("/");
		cookie.setSecure(true);
		cookie.setAttribute("SameSite", "None");
		return cookie;
	}
}
