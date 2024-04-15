package com.woowa.user.domain;

import java.time.Instant;
import java.util.UUID;

import com.woowa.common.domain.NotAuthorizedException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "email_verification_id")
	private Long id;

	@Column(length = 6)
	private String token;

	private Instant expiryDate;

	private String verificationHash;

	private Long userId;

	public EmailVerification(String token, Instant expiryDate, Long userId) {
		this.token = token;
		this.expiryDate = expiryDate;
		this.userId = userId;
	}

	public void checkExpiredToken() {
		if (Instant.now().isAfter(this.expiryDate))
			throw new NotAuthorizedException("이메일 인증 토큰의 만료 시간이 지났습니다.");
	}

	public void checkEqualsToken(String token) {
		if (!this.token.equals(token)) {
			throw new NotAuthorizedException("이메일 인증 코드가 서로 다릅니다.");
		}
	}

	public String configureVerificationHash() {
		this.verificationHash = UUID.randomUUID().toString();
		return this.verificationHash;
	}
}
