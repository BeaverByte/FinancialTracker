package com.beaverbyte.financial_tracker_application.service;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beaverbyte.financial_tracker_application.exception.TokenRefreshException;
import com.beaverbyte.financial_tracker_application.model.RefreshToken;
import com.beaverbyte.financial_tracker_application.model.User;
import com.beaverbyte.financial_tracker_application.repository.RefreshTokenRepository;
import com.beaverbyte.financial_tracker_application.repository.UserRepository;

@Service
public class RefreshTokenService {
	private static final Logger log = LoggerFactory.getLogger(RefreshTokenService.class);

	@Value("${JWT_REFRESH_EXPIRATION_MS}")
	private Long refreshTokenDurationMs;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private UserRepository userRepository;

	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	public Optional<RefreshToken> findByUser(User user) {
		return refreshTokenRepository.findByUser(user);
	}

	public RefreshToken createRefreshToken(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new NoSuchElementException("User not found with ID"));

		RefreshToken refreshToken = RefreshToken.builder()
				.user(user)
				.expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
				.token(UUID.randomUUID().toString())
				.build();

		refreshToken = refreshTokenRepository.save(refreshToken);
		return refreshToken;
	}

	public RefreshToken verifyExpiration(RefreshToken token) {
		if (isExpired(token)) {
			refreshTokenRepository.delete(token);
			throw new TokenRefreshException(token.getToken(),
					"Refresh token was expired. Please make a new signin request");
		}

		return token;
	}

	public boolean isExpired(RefreshToken token) {
		return token.getExpiryDate().compareTo(Instant.now()) < 0;
	}

	@Transactional
	public int deleteByUserId(Long userId) {
		log.info("Deleting Refresh Token from database");

		return userRepository.findById(userId)
				.map(refreshTokenRepository::deleteByUser)
				.orElseThrow(() -> new NoSuchElementException("User not found with ID"));
	}

	public boolean existsByUserId(Long userId) {
		return userRepository.findById(userId)
				.map(user -> {
					boolean userExists = findByUser(user).isPresent();
					if (!userExists) {
						log.debug("No refresh token found for user with ID: {}", userId);
					}
					return userExists;
				})
				.orElseGet(() -> {
					log.debug("User not found with ID: {}", userId);
					return false;
				});
	}
}