package com.woowa.user.domain;

import java.time.Instant;

import com.woowa.common.domain.NotAuthorizedException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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

	private Long userId;

	public EmailVerification(String token, Instant expiryDate, Long userId) {
		this.token = token;
		this.expiryDate = expiryDate;
		this.userId = userId;
	}

	public void checkExpired() {
		if (this.expiryDate.isAfter(Instant.now()))
			throw new NotAuthorizedException("이메일 인증 토큰의 만료 시간이 지났습니다.");
	}
}
