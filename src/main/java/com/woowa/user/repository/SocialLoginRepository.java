package com.woowa.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.woowa.user.domain.SocialLogin;

public interface SocialLoginRepository extends JpaRepository<SocialLogin, Long> {
	Optional<SocialLogin> findByExternalId(String externalId);
	
}
