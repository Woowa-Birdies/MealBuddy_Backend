package com.woowa.user.domain;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.DynamicUpdate;

import com.woowa.common.domain.DuplicateException;
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
@DynamicUpdate
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

	public EmailVerification(String token, Long userId) {
		this.token = token;
		this.userId = userId;
		plusExpiredDate();
	}

	public void checkExpiredToken() {
		if (Instant.now().isAfter(this.expiryDate))
			throw new NotAuthorizedException("이메일 인증 토큰의 만료 시간이 지났습니다.");
	}

	public String configureVerificationHash() {
		this.verificationHash = UUID.randomUUID().toString();
		return this.verificationHash;
	}

	public void updateToken(String token) {
		this.token = token;
		plusExpiredDate();
	}

	public void plusExpiredDate() {
		this.expiryDate = Instant.now().plusSeconds(300); // 5분
	}

	public void checkVerificationBefore() {
		if (this.verificationHash != null) {
			throw new DuplicateException(this.id, "EmailVerification");
		}
	}
}
