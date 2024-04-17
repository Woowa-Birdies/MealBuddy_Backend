package com.woowa.user.controller;

import com.woowa.user.service.dto.EmailVerificationDTO;

public interface EmailUseCase {

	void sendEmail(Long userId, String toEmail);

	void verifyEmailToken(EmailVerificationDTO emailVerificationDTO);
}
