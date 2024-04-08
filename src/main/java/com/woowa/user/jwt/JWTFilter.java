package com.woowa.user.jwt;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.web.filter.OncePerRequestFilter;

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
		//request에서 Authorization 헤더를 찾음

		String accessToken = request.getHeader("Authorization");

		//Authorization 헤더 검증
		if (accessToken == null) {

			filterChain.doFilter(request, response);

			//조건이 해당되면 메소드 종료 (필수)
			return;
		}

		//토큰 소멸 시간 검증
		try {
			jwtUtil.isExpired(accessToken);
		} catch (ExpiredJwtException e) {
			PrintWriter writer = response.getWriter();
			writer.println("access token expired");

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		Long userId = jwtUtil.getUserId(accessToken);
		String role = jwtUtil.getRole(accessToken);

	}
}
