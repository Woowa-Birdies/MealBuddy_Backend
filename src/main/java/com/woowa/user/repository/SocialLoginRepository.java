package com.woowa.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.woowa.user.domain.SocialLogin;

public interface SocialLoginRepository extends JpaRepository<SocialLogin, Long> {
	Optional<SocialLogin> findByExternalId(String externalId);

	Boolean existsByRefreshToken(String refreshToken);

	Optional<SocialLogin> findByRefreshToken(String refreshToken);

	@Modifying(clearAutomatically = true)
	@Query("delete from SocialLogin s "
		+ "where s.refreshToken = :refreshToken")
	void deleteByRefreshToken(@Param("refreshToken") String refreshToken);
}
