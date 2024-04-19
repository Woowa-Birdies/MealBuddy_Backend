package com.woowa.user.controller;

import static com.woowa.common.domain.SecurityConstant.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import com.woowa.IntegrationTestSupport;
import com.woowa.common.domain.ResourceNotFoundException;
import com.woowa.user.domain.SocialLogin;
import com.woowa.user.jwt.JWTUtil;
import com.woowa.user.repository.SocialLoginRepository;

import jakarta.servlet.http.Cookie;

class AuthControllerTest extends IntegrationTestSupport {

	@Autowired
	private SocialLoginRepository socialLoginRepository;

	@Autowired
	private JWTUtil jwtUtil;

	@Test
	@DisplayName("사용자는 로그아웃을 할 수 있다")
	@WithMockUser
	void 사용자는_로그아웃을_할_수_있다() throws Exception {
		//given
		SocialLogin socialLogin = new SocialLogin(1L, "KAKAO", "asdsadsad");
		String refreshToken = jwtUtil.createJwt(REFRESH_TOKEN, socialLogin.getUserId(), ROLE_USER,
			REFRESH_TOKEN_DURATION);
		socialLogin.update(refreshToken);
		SocialLogin savedSocialLogin = socialLoginRepository.save(socialLogin);
		System.out.println("refreshToken test : " + savedSocialLogin.getRefreshToken().equals(refreshToken));
		System.out.println("savedSocialLogin.getId() = " + savedSocialLogin.getId());

		Cookie cookie = new Cookie(REFRESH_TOKEN, refreshToken);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");

		cookie.setMaxAge(Math.toIntExact(REFRESH_TOKEN_DURATION));
		//when
		//then
		mockMvc.perform(post("/api/logout")
				.cookie(cookie))
			.andExpect(status().isOk());

		Assertions.assertThatThrownBy(() -> socialLoginRepository.findById(savedSocialLogin.getId())
				.orElseThrow(() -> new ResourceNotFoundException(socialLogin.getId(), "SocialLogin")))
			.isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	@DisplayName("사용자는 액세스 토큰과 리프레시 토큰을 갱신할 수 있다")
	@WithMockUser
	void 사용자는_액세스_토큰과_리프레시_토큰을_갱신할_수_있다() throws Exception {
		//given
		SocialLogin socialLogin = new SocialLogin(1L, "KAKAO", "asdsadsad");
		String refreshToken = jwtUtil.createJwt(REFRESH_TOKEN, socialLogin.getUserId(), ROLE_USER,
			REFRESH_TOKEN_DURATION);
		socialLogin.update(refreshToken);
		SocialLogin savedSocialLogin = socialLoginRepository.save(socialLogin);
		System.out.println("refreshToken test : " + savedSocialLogin.getRefreshToken().equals(refreshToken));
		System.out.println("savedSocialLogin.getId() = " + savedSocialLogin.getId());

		Cookie cookie = new Cookie(REFRESH_TOKEN, refreshToken);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(Math.toIntExact(REFRESH_TOKEN_DURATION));
		//when
		//then
		mockMvc.perform(post("/reissue")
				.cookie(cookie))
			.andExpect(status().isOk());
		SocialLogin updateSocialLogin = socialLoginRepository.findById(savedSocialLogin.getId()).get();
		Assertions.assertThat(updateSocialLogin.getRefreshToken()).isNotEqualTo(refreshToken);
	}
}