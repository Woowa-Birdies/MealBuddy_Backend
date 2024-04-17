package com.woowa.user.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {

	@Value("${backend.url}")
	private String url;

	public String createHttpOnlyCookie(String key, String value, Long age) {
		ResponseCookie cookie = ResponseCookie.from(key, value)
			.path("/")
			.sameSite("none")
			.httpOnly(true)
			.domain(url)
			.secure(true)
			.maxAge(age)
			.build();
		return cookie.toString();
	}

	public String createCookie(String key, String value, Long age) {
		ResponseCookie cookie = ResponseCookie.from(key, value)
			.path("/")
			.sameSite("none")
			.secure(true)
			.domain(url)
			.maxAge(age)
			.build();
		return cookie.toString();
	}
	// public Cookie createHttpOnlyCookie(String key, String value, Long age) {
	// 	Cookie cookie = new Cookie(key, value);
	// 	cookie.setMaxAge(Math.toIntExact(age));
	// 	cookie.setPath("/");
	// 	cookie.setSecure(true);
	// 	cookie.setHttpOnly(true);
	// 	cookie.setAttribute("SameSite", "None");
	// 	return cookie;
	//  }
	//
	// public Cookie createCookie(String key, String value, Long age) {
	// 	Cookie cookie = new Cookie(key, value);
	// 	cookie.setMaxAge(Math.toIntExact(age));
	// 	cookie.setPath("/");
	// 	cookie.setSecure(true);
	// 	cookie.setAttribute("SameSite", "None");
	// 	return cookie;
	// }
}
