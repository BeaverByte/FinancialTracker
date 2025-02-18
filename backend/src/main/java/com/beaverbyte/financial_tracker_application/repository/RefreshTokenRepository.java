package com.beaverbyte.financial_tracker_application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.beaverbyte.financial_tracker_application.model.RefreshToken;
import com.beaverbyte.financial_tracker_application.model.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByToken(String token);

	Optional<RefreshToken> findByUser(User user);

	@Modifying
	int deleteByUser(User user);

}
