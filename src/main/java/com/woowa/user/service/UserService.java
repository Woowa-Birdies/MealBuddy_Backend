package com.woowa.user.service;

import org.springframework.stereotype.Service;

import com.woowa.common.domain.DuplicateException;
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

}
