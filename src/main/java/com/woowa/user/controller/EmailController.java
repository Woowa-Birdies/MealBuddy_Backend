package com.woowa.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.woowa.user.service.EmailService;

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
}
