package com.woowa.user.service;

import static com.woowa.common.domain.SecurityConstant.*;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.common.domain.NotAuthorizedException;
import com.woowa.common.domain.ResourceNotFoundException;
import com.woowa.user.domain.SocialLogin;
import com.woowa.user.jwt.JWTUtil;
import com.woowa.user.repository.SocialLoginRepository;
import com.woowa.user.util.CookieUtils;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private final JWTUtil jwtUtil;
	private final CookieUtils cookieUtils;
	private final SocialLoginRepository socialLoginRepository;

	@Transactional
	public void updateRefreshToken(String refreshToken) {
		SocialLogin socialLogin = socialLoginRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new ResourceNotFoundException("refreshToken", "SocialLogin"));

		socialLogin.update(refreshToken);
	}

	@Transactional
	public void logout(String refreshToken) {
		socialLoginRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new NotAuthorizedException("이미 로그아웃되었습니다."));
		socialLoginRepository.deleteByRefreshToken(refreshToken);

		cookieUtils.createHttpOnlyCookie(refreshToken, null, 0L);
	}

	public boolean isInvalidRefreshToken(Optional<String> refreshTokenOpt) {
		return refreshTokenOpt.map(
				refreshToken -> isExpired(refreshToken)
					|| isNotRefreshToken(refreshToken)
					|| isNotExistRefreshToken(refreshToken))
			.orElse(true);
	}

	private boolean isNotExistRefreshToken(String refreshToken) {
		return !socialLoginRepository.existsByRefreshToken(refreshToken);
	}

	private boolean isNotRefreshToken(String refreshToken) {
		String category = jwtUtil.getCategory(refreshToken);

		return !category.equals(REFRESH_TOKEN);
	}

	private boolean isExpired(String refreshToken) {
		try {
			jwtUtil.isExpired(refreshToken);
		} catch (ExpiredJwtException e) {
			return true;
		}
		return false;
	}
}
