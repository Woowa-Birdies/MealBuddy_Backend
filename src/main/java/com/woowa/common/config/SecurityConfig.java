package com.woowa.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import com.woowa.user.handler.CustomSuccessHandler;
import com.woowa.user.jwt.JWTUtil;
import com.woowa.user.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomSuccessHandler customSuccessHandler;
	private final JWTUtil jwtUtil;

	@Value("${cors.allowedOriginPatterns}")
	private String corsAllowOriginPatterns;

	@Value("${cors.allowedMethods}")
	private String allowedMethods;

	@Value("${cors.allowedHeaders}")
	private String allowedHeaders;

	@Value("${cors.exposedHeaders}")
	private String exposedHeaders;

	@Value("${cors.maxAge}")
	private Long maxAge;

	@Bean
	@ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true")
	public WebSecurityCustomizer configureH2ConsoleEnable() {
		return web -> web.ignoring()
				.requestMatchers(PathRequest.toH2Console());
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		disableAnotherLogin(http);

		configureCors(http);

		configureOAuth(http);

		configureMapping(http);

		configureSession(http);

		return http.build();
	}

	private static void configureSession(HttpSecurity http) throws Exception {
		http
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	}

	private static void configureMapping(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/actuator/health", "/login/**", "/oauth2/**", "/gather/**", "/post/**").permitAll()
				.anyRequest().authenticated());
	}

	private void configureOAuth(HttpSecurity http) throws Exception {
		http
			.oauth2Login(oauth2 -> oauth2
				.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
					.userService(customOAuth2UserService))
				.successHandler(customSuccessHandler));
	}

	private void configureCors(HttpSecurity http) throws Exception {
		http
			.cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {

				CorsConfiguration configuration = new CorsConfiguration();

				configuration.setAllowedOriginPatterns(List.of(corsAllowOriginPatterns.split(",")));
				configuration.setAllowedMethods(List.of(allowedMethods.split(",")));
				configuration.setAllowCredentials(true);
				configuration.setAllowedHeaders(List.of(allowedHeaders.split(",")));
				configuration.setMaxAge(maxAge);

				configuration.setExposedHeaders(List.of(exposedHeaders.split(",")));

				return configuration;
			}));
	}

	private static void disableAnotherLogin(HttpSecurity http) throws Exception {
		// csrf disable @TODO 배포 전에 CSRF 활성화해보기
		http
			.csrf(AbstractHttpConfigurer::disable);

		http
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.oauth2Login(Customizer.withDefaults());
	}
}
