package com.woowa.gather.domain.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.woowa.gather.domain.enums.AskStatus;
import com.woowa.user.domain.Gender;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostAskListResponse {

	private Long userId;
	private AskStatus askStatus;
	private Gender gender;
	private int age;
	private String introduce;

	public PostAskListResponse(Long userId, AskStatus askStatus, Gender gender, LocalDateTime birthDate,
		String introduce) {
		this.userId = userId;
		this.askStatus = askStatus;
		this.gender = gender;
		this.age = LocalDate.now().getYear() - birthDate.getYear() + 1;
		this.introduce = introduce;
	}
}
