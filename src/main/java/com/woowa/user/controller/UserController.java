package com.woowa.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.woowa.common.domain.DuplicateException;
import com.woowa.user.domain.dto.SignupRequest;
import com.woowa.user.domain.dto.UpdateProfileRequest;
import com.woowa.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping("/check/{nickname}")
	public ResponseEntity<Void> checkDuplicateNickName(@PathVariable("nickname") String nickName) {
		userService.findByNickname(nickName).ifPresent((user) -> {
			throw new DuplicateException(nickName, "User");
		});
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping("/signup")
	public ResponseEntity<Long> addInfo(@RequestBody @Valid SignupRequest request) {
		return ResponseEntity.ok(userService.setAdditionalInfo(request));
	}

	@PatchMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Long> updateProfile(@RequestBody @Valid UpdateProfileRequest request) {
		return ResponseEntity.ok(userService.updateProfile(request));
	}
}
