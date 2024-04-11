package com.woowa.user.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
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
}
