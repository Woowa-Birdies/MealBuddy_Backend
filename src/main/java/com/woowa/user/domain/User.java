package com.woowa.user.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicUpdate;

import com.woowa.common.domain.BaseEntity;
import com.woowa.user.domain.dto.SignupRequest;
import com.woowa.user.domain.dto.UpdateProfileRequest;

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
@DynamicUpdate
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
	private LocalDateTime birthDate;

	@Column(length = 8)
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(length = 36)
	private String email;

	@Column
	private String image;

	public User(String nickname) {
		this.nickname = nickname;
	}

	public void update(String nickname) {
		this.nickname = nickname;
	}

	public void updateAdditionalInfo(SignupRequest request) {
		this.nickname = request.getNickname();
		this.birthDate = request.getBirthDate();
		this.gender = request.getGender();
		this.email = request.getEmail();
	}

	public void update(UpdateProfileRequest request) {
		this.nickname = request.getNickname();
		this.introduce = request.getIntroduce();
	}
}
