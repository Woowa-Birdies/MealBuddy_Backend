package com.woowa.user.handler;

import static com.woowa.common.domain.SecurityConstant.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.woowa.user.domain.dto.CustomOAuth2User;
import com.woowa.user.jwt.JWTUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JWTUtil jwtUtil;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		CustomOAuth2User customUserDetails = (CustomOAuth2User)authentication.getPrincipal();

		Long userId = customUserDetails.getUserId();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		String role = auth.getAuthority();

		String accessToken = jwtUtil.createJwt(ACCESS_TOKEN, userId, role, ACCESS_TOKEN_DURATION);
		String refreshToken = jwtUtil.createJwt(REFRESH_TOKEN, userId, role, ACCESS_TOKEN_DURATION);

		response.setHeader(AUTHORIZATION, BEARER + accessToken);
		response.addCookie(createCookie(REFRESH_TOKEN, refreshToken, REFRESH_TOKEN_DURATION));
		response.sendRedirect("http://localhost:3000/");
	}

	private Cookie createCookie(String key, String value, Long age) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(Math.toIntExact(age));
		cookie.setPath("/");
		cookie.setHttpOnly(true);

		return cookie;
	}
}
