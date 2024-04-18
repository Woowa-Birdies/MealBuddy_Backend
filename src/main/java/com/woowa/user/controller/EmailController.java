package com.woowa.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.woowa.user.service.EmailService;
import com.woowa.user.service.dto.EmailVerificationDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
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
	public ResponseEntity<String> verifyEmail(@RequestBody @Valid EmailVerificationDTO emailVerificationDTO) {
		return ResponseEntity.ok(emailService.verifyEmailToken(emailVerificationDTO));
	}
}
