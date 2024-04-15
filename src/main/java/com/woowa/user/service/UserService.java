package com.woowa.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.common.domain.DuplicateException;
import com.woowa.common.domain.NotAuthorizedException;
import com.woowa.common.domain.ResourceNotFoundException;
import com.woowa.user.domain.User;
import com.woowa.user.domain.dto.SignupRequest;
import com.woowa.user.repository.EmailRepository;
import com.woowa.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
	private final EmailRepository emailRepository;

	public Optional<User> findByNickname(String nickName) {
		return userRepository.findByNickname(nickName);
	}

	@Transactional
	public Long setAdditionalInfo(SignupRequest signupRequest) {
		User user = getByUserId(signupRequest.getUserId());
		userRepository.findByEmail(signupRequest.getEmail()).ifPresent(anotherUser -> {
			throw new DuplicateException(signupRequest.getEmail(), "User");
		});
		emailRepository.findByVerificationHash(signupRequest.getVerificationHash())
			.orElseThrow(() -> new NotAuthorizedException("이메일 인증을 완료해주세요."));

		user.updateAdditionalInfo(signupRequest);

		return user.getId();
	}

	private User getByUserId(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(userId, "User"));
	}
}
