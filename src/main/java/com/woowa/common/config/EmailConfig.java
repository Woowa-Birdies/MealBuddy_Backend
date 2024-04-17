package com.woowa.common.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {
	@Value("${spring.mail.username}")
	private String username;
	@Value("${spring.mail.password}")
	private String password;
	@Value("${spring.mail.host}")
	private String host;
	@Value("${spring.mail.port}")
	private int port;

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

		javaMailSender.setHost(host);
		javaMailSender.setPort(port);
		javaMailSender.setUsername(username);
		javaMailSender.setPassword(password);
		javaMailSender.setJavaMailProperties(getMailProperties());
		javaMailSender.setDefaultEncoding("UTF-8");
		return javaMailSender;
	}

	private Properties getMailProperties() {
		Properties properties = new Properties();
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.debug", "true");
		properties.setProperty("mail.smtp.ssl.trust", host);
		// TODO: HTTPS 설정 해보기
		properties.setProperty("mail.smtp.ssl.enable", "false");

		return properties;
	}
}
