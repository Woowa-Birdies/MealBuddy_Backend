package com.woowa.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.woowa.user.domain.EmailVerification;

public interface EmailRepository extends JpaRepository<EmailVerification, Long> {
	Optional<EmailVerification> findByUserId(Long userId);
}
