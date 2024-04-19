package com.woowa.user.controller;

import static com.woowa.common.domain.SecurityConstant.*;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.woowa.common.domain.NotAuthorizedException;
import com.woowa.user.controller.dto.MeResponse;
import com.woowa.user.domain.dto.CustomOAuth2User;
import com.woowa.user.service.AuthService;
import com.woowa.user.service.TokenGenerator;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
	private final TokenGenerator tokenGenerator;
	private final AuthService authService;

	@PostMapping("/reissue")
	public ResponseEntity<String> reissue(@CookieValue(name = REFRESH_TOKEN) Optional<String> refreshToken,
		HttpServletResponse response) {

		if (authService.isInvalidRefreshToken(refreshToken)) {
			throw new NotAuthorizedException("리프레시 처리 과정 중 에러가 있습니다.");
		}

		return ResponseEntity.ok(tokenGenerator.generateTokens(response, refreshToken.get()));
	}

	@PostMapping("/api/logout")
	public ResponseEntity<Void> logout(@CookieValue(name = REFRESH_TOKEN) Optional<String> refreshToken) {
		if (refreshToken.isPresent()) {
			authService.logout(refreshToken.get());
		} else {
			throw new NotAuthorizedException("리프레시 토큰이 존재하지 않습니다.");
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping("/me")
	public ResponseEntity<MeResponse> me() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();

		// TODO: 알림 필요하다면 여기에 추가
		return ResponseEntity.ok(new MeResponse(customOAuth2User.getUserId()));
	}
}
