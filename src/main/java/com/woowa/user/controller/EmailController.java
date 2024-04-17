package com.woowa.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.woowa.user.service.EmailService;
import com.woowa.user.service.dto.EmailVerificationDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class EmailController {

	private final EmailService emailService;

	@GetMapping("/email/{toEmail}/verifications/{userId}")
	public ResponseEntity<Void> sendEmail(@PathVariable("toEmail") String toEmail,
		@PathVariable("userId") Long userId) {
		emailService.sendEmail(userId, toEmail);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/email/verifications")
	public ResponseEntity<Void> verifyEmail(@RequestBody @Valid EmailVerificationDTO emailVerificationDTO) {
		emailService.verifyEmailToken(emailVerificationDTO);
		return ResponseEntity.ok().build();
	}
}
