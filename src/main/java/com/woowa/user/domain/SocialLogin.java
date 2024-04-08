package com.woowa.user.domain;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialLogin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;

	@Enumerated(EnumType.STRING)
	private SocialCode socialCode;

	private String externalId;

	private String token;

	public SocialLogin(Long userId, String socialCode, String externalId) {
		this.userId = userId;
		this.socialCode = SocialCode.toCode(socialCode);
		this.externalId = externalId;
	}

}
