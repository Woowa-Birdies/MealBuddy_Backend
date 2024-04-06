package com.woowa.user.domain;

import com.woowa.common.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
	@Id
	@Column(name = "user_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 16)
	private String nickname;

	@Column(columnDefinition = "text")
	private String introduce;

	@Column
	private int age;

	@Column(length = 8)
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(length = 128)
	private String phone;

	public User(String nickname) {
		this.nickname = nickname;
	}

	public void update(String nickname) {
		this.nickname = nickname;
	}
}
