package com.woowa.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.woowa.IntegrationTestSupport;
import com.woowa.user.domain.Gender;
import com.woowa.user.domain.User;
import com.woowa.user.domain.dto.SignupRequest;
import com.woowa.user.repository.UserRepository;

class UserControllerTest extends IntegrationTestSupport {

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("중복된 닉네임이 있으면 에러가 발생한다")
	@WithMockUser
	void 중복된_닉네임이_있으면_에러가_발생한다() throws Exception {
		//given
		userRepository.save(new User("test"));
		//when
		//then
		mockMvc.perform(get("/check/test"))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("중복된 닉네임이 없으면 닉네임을 사용할 수 있다")
	@WithMockUser
	void 중복된_닉네임이_없으면_닉네임을_사용할_수_있다() throws Exception {
		//given
		//when
		//then
		mockMvc.perform(get("/check/test"))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("사용자는 추가적인 정보를 작성할 수 있다")
	@WithMockUser
	void 사용자는_추가적인_정보를_작성할_수_있다() throws Exception {
		//given
		User savedUser = userRepository.save(new User("temp"));
		LocalDateTime now = LocalDateTime.now();
		SignupRequest request = new SignupRequest(savedUser.getId(), "test2", now, Gender.OTHER, "test22@test.co.kr");
		objectMapper.registerModule(new JavaTimeModule());
		//when
		//then
		mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());

		User user = userRepository.findByNickname("test2").get();
		Assertions.assertThat(user.getNickname()).isEqualTo(request.getNickname());
		Assertions.assertThat(user.getBirthDate()).isEqualTo(now);
		Assertions.assertThat(user.getGender()).isEqualTo(Gender.OTHER);
		Assertions.assertThat(user.getEmail()).isEqualTo(request.getEmail());
	}
}