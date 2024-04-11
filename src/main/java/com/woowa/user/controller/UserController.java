package com.woowa.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.woowa.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping("/check/{nickname}")
	public ResponseEntity<Void> checkDuplicateNickName(@PathVariable("nickname") String nickName) {
		userService.checkDuplicateNickname(nickName);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
