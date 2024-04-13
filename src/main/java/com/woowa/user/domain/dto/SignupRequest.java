package com.woowa.user.domain.dto;

import java.time.LocalDateTime;

import com.woowa.user.domain.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {
	@NotNull
	private Long userId;
	@NotEmpty
	private String nickname;
	@Past
	private LocalDateTime birthDate;
	private Gender gender;
	@Email
	private String email;

	public SignupRequest(Long userId, String nickname, LocalDateTime birthDate, Gender gender, String email) {
		this.userId = userId;
		this.nickname = nickname;
		this.birthDate = birthDate;
		this.gender = gender;
		this.email = email;
	}
}
