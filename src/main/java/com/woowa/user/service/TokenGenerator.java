package com.woowa.user.service;

import static com.woowa.common.domain.SecurityConstant.*;

import java.util.Random;

import org.springframework.stereotype.Component;

import com.woowa.user.jwt.JWTUtil;
import com.woowa.user.util.CookieUtils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenGenerator {
	private final JWTUtil jwtUtil;
	private final AuthService authService;
	private final CookieUtils cookieUtils;
	private final Random random = new Random();

	public String generateTokens(HttpServletResponse response, String refreshToken) {
		Long userId = jwtUtil.getUserId(refreshToken);
		String role = jwtUtil.getRole(refreshToken);

		String newAccessToken = jwtUtil.createJwt(ACCESS_TOKEN, userId, role, ACCESS_TOKEN_DURATION);
		String newRefreshToken = jwtUtil.createJwt(REFRESH_TOKEN, userId, role, ACCESS_TOKEN_DURATION);
		authService.updateRefreshToken(newRefreshToken, refreshToken);
		response.setHeader(AUTHORIZATION, BEARER + newAccessToken);
		response.addHeader("Set-Cookie",
			cookieUtils.createHttpOnlyCookie(REFRESH_TOKEN, refreshToken, REFRESH_TOKEN_DURATION));

		return "Success generating tokens";
	}

	public String generateEmailVerificationToken() {
		return String.format("%06d", random.nextInt(1000000));
	}

}
