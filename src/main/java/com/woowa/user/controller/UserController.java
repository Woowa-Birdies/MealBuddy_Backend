package com.woowa.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.woowa.common.domain.DuplicateException;
import com.woowa.user.controller.dto.UserProfileResponse;
import com.woowa.user.domain.dto.SignupRequest;
import com.woowa.user.domain.dto.UpdateProfileRequest;
import com.woowa.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
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

	@PatchMapping("/profile")
	public ResponseEntity<Long> updateProfile(@RequestBody @Valid UpdateProfileRequest request) {
		return ResponseEntity.ok(userService.updateProfile(request));
	}

	@GetMapping("/profile/{userId}")
	public ResponseEntity<UserProfileResponse> getProfile(@PathVariable("userId") Long userId) {
		return ResponseEntity.ok(UserProfileResponse.toResponse(userService.getById(userId)));
	}
}
