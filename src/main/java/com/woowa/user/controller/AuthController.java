package com.woowa.user.controller;

import static com.woowa.common.domain.SecurityConstant.*;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.woowa.user.controller.dto.MeResponse;
import com.woowa.user.domain.dto.CustomOAuth2User;
import com.woowa.user.service.AuthService;
import com.woowa.user.service.TokenGenerator;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
	private final TokenGenerator tokenGenerator;
	private final AuthService authService;

	@PostMapping("/reissue")
	public ResponseEntity<String> reissue(HttpServletRequest request, HttpServletResponse response) {
		String INVALID_ERROR_MESSAGE = "Invalid refresh token";

		Optional<String> refreshToken = Arrays.stream(request.getCookies())
			.filter(cookie -> cookie.getName().equals(REFRESH_TOKEN))
			.map(Cookie::getValue)
			.findAny();
		return authService.isInvalidRefreshToken(refreshToken) ?
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body(INVALID_ERROR_MESSAGE) :
			ResponseEntity.ok(tokenGenerator.generateTokens(response, refreshToken.get()));
	}

	@GetMapping("/me")
	public ResponseEntity<MeResponse> me() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
 
		// TODO: 알림 필요하다면 여기에 추가
		return ResponseEntity.ok(new MeResponse(customOAuth2User.getUserId()));
	}
}
