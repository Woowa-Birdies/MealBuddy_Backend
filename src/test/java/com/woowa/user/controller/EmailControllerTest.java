package com.woowa.user.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.woowa.IntegrationTestSupport;
import com.woowa.user.domain.EmailVerification;
import com.woowa.user.domain.User;
import com.woowa.user.repository.EmailRepository;
import com.woowa.user.repository.UserRepository;
import com.woowa.user.service.dto.EmailVerificationDTO;

class EmailControllerTest extends IntegrationTestSupport {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailRepository emailRepository;

	@Test
	@DisplayName("사용자는 이메일 인증을 할 수 있다")
	@WithMockUser
	void 사용자는_이메일_인증을_할_수_있다() throws Exception {
		//given
		User testUser = userRepository.save(new User("test"));
		//when
		//then
		mockMvc.perform(get("/email/ambosing_@naver.com/verifications/{userId}", testUser.getId()))
			.andExpect(status().isOk());
		EmailVerification emailVerification = emailRepository.findByUserId(testUser.getId()).get();

		assertThat(emailVerification).isNotNull();
	}

	@Test
	@DisplayName("사용자는 인증 번호를 입력해 이메일 인증을 완료할 수 있다")
	@WithMockUser
	void 사용자는_인증_번호를_입력해_이메일_인증을_완료할_수_있다() throws Exception {
		//given
		User testUser = userRepository.save(new User("test"));
		EmailVerification emailVerification = emailRepository.save(
			new EmailVerification("123456", testUser.getId()));
		EmailVerificationDTO emailVerificationDTO = new EmailVerificationDTO(testUser.getId(),
			emailVerification.getToken());
		objectMapper.registerModule(new JavaTimeModule());
		//when
		//then
		MvcResult result = mockMvc.perform(post("/email/verifications")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(emailVerificationDTO)))
			.andExpect(status().isOk())
			.andReturn();

		String responseContent = result.getResponse().getContentAsString();
		assertThat(responseContent).isNotNull();
		assertThat(responseContent).isNotEmpty();
	}
}