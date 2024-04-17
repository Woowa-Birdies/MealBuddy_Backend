package com.woowa.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

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

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOriginPatterns(corsAllowOriginPatterns)
			.allowedMethods(allowedMethods)
			.allowedHeaders(allowedHeaders)
			.exposedHeaders(exposedHeaders)
			.maxAge(maxAge);
	}
}
