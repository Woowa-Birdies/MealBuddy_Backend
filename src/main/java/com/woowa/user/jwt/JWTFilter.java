package com.woowa.user.jwt;

import static com.woowa.common.domain.SecurityConstant.*;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.woowa.common.domain.NotAuthorizedException;
import com.woowa.user.domain.dto.CustomOAuth2User;
import com.woowa.user.domain.dto.UserDTO;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String authorizationHeader = request.getHeader(AUTHORIZATION);
		if (isAuthorizationHeaderNull(request, response, filterChain, authorizationHeader))
			return;

		String accessToken = authorizationHeader.substring(7);

		if (isExpired(accessToken))
			return;

		CustomOAuth2User customUserDetails = getCustomOAuth2User(accessToken);

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
			customUserDetails, null, customUserDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);
	}

	private CustomOAuth2User getCustomOAuth2User(String accessToken) {
		Long userId = jwtUtil.getUserId(accessToken);
		String role = jwtUtil.getRole(accessToken);

		return new CustomOAuth2User(new UserDTO(userId, role));
	}

	private static boolean isAuthorizationHeaderNull(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain,
		String authorizationHeader) throws IOException, ServletException {
		//Authorization 헤더 검증
		if (authorizationHeader == null) {

			filterChain.doFilter(request, response);

			//조건이 해당되면 메소드 종료 (필수)
			return true;
		}
		return false;
	}

	private boolean isExpired(String accessToken) {
		try {
			jwtUtil.isExpired(accessToken);
		} catch (ExpiredJwtException e) {
			throw new NotAuthorizedException("액세스 토큰 만료");
		}
		return false;
	}
}
