package com.woowa.user.fake;

import java.io.InputStream;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.internet.MimeMessage;

public class FakeJavaMailSender implements JavaMailSender {

	@Override
	public MimeMessage createMimeMessage() {
		return null;
	}

	@Override
	public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
		return null;
	}

	@Override
	public void send(MimeMessage... mimeMessages) throws MailException {
		
	}

	@Override
	public void send(SimpleMailMessage... simpleMessages) throws MailException {

	}
}