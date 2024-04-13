package com.woowa.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.common.domain.DuplicateException;
import com.woowa.user.domain.User;
import com.woowa.user.domain.dto.SignupRequest;
import com.woowa.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;

	public void checkDuplicateNickname(String nickName) {
		userRepository.findByNickname(nickName).ifPresent((item) -> {
			throw new DuplicateException(nickName, "User");
		});
	}

	@Transactional
	public Long setAdditionalInfo(SignupRequest signupRequest) {
		checkDuplicateNickname(signupRequest.getNickname());
		User user = userRepository.findByEmail(signupRequest.getEmail())
			.orElseThrow(() -> new DuplicateException(signupRequest.getEmail(), "User"));

		user.updateAdditionalInfo(signupRequest);
		return user.getId();
	}
}
