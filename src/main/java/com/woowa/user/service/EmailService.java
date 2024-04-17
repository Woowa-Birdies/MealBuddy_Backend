package com.woowa.user.service;

import java.io.UnsupportedEncodingException;
import java.time.Instant;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.common.domain.DuplicateException;
import com.woowa.common.domain.EmailException;
import com.woowa.common.domain.NotAuthorizedException;
import com.woowa.user.domain.EmailVerification;
import com.woowa.user.repository.EmailRepository;
import com.woowa.user.service.dto.EmailVerificationDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender javaMailSender;
	private final EmailRepository emailRepository;
	private final TokenGenerator tokenGenerator;

	public void sendEmail(Long userId, String toEmail) {
		emailRepository.findByUserId(userId).ifPresent((item) -> {
			throw new DuplicateException(userId, "EmailVerification");
		});
		String token = tokenGenerator.generateEmailVerificationToken();

		try {
			javaMailSender.send(createVerificationMessage(toEmail, token));
			emailRepository.save(new EmailVerification(token, Instant.now(), userId));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new EmailException();
		}
	}

	@Transactional
	public String verifyEmailToken(EmailVerificationDTO emailVerificationDTO) {
		EmailVerification emailVerification = emailRepository.findByToken(emailVerificationDTO.getToken())
			.orElseThrow(() -> new NotAuthorizedException("이메일 인증에 실패하였습니다."));
		emailVerification.checkExpiredToken();
		
		return emailVerification.configureVerificationHash();
	}

	private MimeMessage createVerificationMessage(String toEmail, String token) throws
		MessagingException,
		UnsupportedEncodingException {
		MimeMessage message = javaMailSender.createMimeMessage();

		message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
		message.setSubject("[냠메이트] 인증 메일입니다.");

		String content = String.format("""
			냠메이트 인증 메일입니다.
			인증을 완료하고 회원가입을 완료하세요.
						
			인증 번호 : %s
						
			냠메이트와 함께 즐거운 냠냠하세요.
			""", token);

		message.setText(content, "UTF-8");
		message.setFrom(new InternetAddress("nyam-mate@gmail.com", "admin"));

		return message;
	}

}
