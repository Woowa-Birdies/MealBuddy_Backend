package com.woowa.user.handler;

import static com.woowa.common.domain.SecurityConstant.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.common.domain.ResourceNotFoundException;
import com.woowa.user.domain.SocialLogin;
import com.woowa.user.domain.dto.CustomOAuth2User;
import com.woowa.user.jwt.JWTUtil;
import com.woowa.user.repository.SocialLoginRepository;
import com.woowa.user.util.CookieUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JWTUtil jwtUtil;

	private final CookieUtils cookieUtils;

	private final SocialLoginRepository socialLoginRepository;

	@Value("${frontend.url}")
	private String frontendUrl;

	@Override
	@Transactional
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		CustomOAuth2User customUserDetails = (CustomOAuth2User)authentication.getPrincipal();

		Long userId = customUserDetails.getUserId();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		String role = auth.getAuthority();

		SocialLogin socialLogin = socialLoginRepository.findByExternalId(customUserDetails.getExternalID())
			.orElseThrow(() -> new ResourceNotFoundException(customUserDetails.getExternalID(), "SocialLogin"));

		String accessToken = jwtUtil.createJwt(ACCESS_TOKEN, userId, role, ACCESS_TOKEN_DURATION);
		System.out.println("accessToken = " + accessToken);
		String refreshToken = jwtUtil.createJwt(REFRESH_TOKEN, userId, role, REFRESH_TOKEN_DURATION);

		response.addHeader("Set-Cookie",
			cookieUtils.createHttpOnlyCookie(REFRESH_TOKEN, refreshToken, REFRESH_TOKEN_DURATION));
		response.addHeader("Set-Cookie",
			cookieUtils.createCookie(ACCESS_TOKEN, accessToken, ACCESS_TOKEN_DURATION));
		socialLogin.update(refreshToken);

		if (customUserDetails.getIsFirstLogin()) {
			response.sendRedirect(frontendUrl + "/verification");
		} else {
			response.sendRedirect(frontendUrl);
		}
	}

}
