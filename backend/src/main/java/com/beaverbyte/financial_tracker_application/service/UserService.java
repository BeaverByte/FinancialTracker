package com.beaverbyte.financial_tracker_application.service;

import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.dto.request.LoginRequest;
import com.beaverbyte.financial_tracker_application.dto.request.SignupRequest;
import com.beaverbyte.financial_tracker_application.dto.response.LoginResponse;
import com.beaverbyte.financial_tracker_application.dto.response.JwtResponse;
import com.beaverbyte.financial_tracker_application.dto.response.MessageResponse;
import com.beaverbyte.financial_tracker_application.dto.response.RefreshTokenResponse;
import com.beaverbyte.financial_tracker_application.dto.response.UserInfoResponse;
import com.beaverbyte.financial_tracker_application.exception.TokenRefreshException;
import com.beaverbyte.financial_tracker_application.mapper.UserMapper;
import com.beaverbyte.financial_tracker_application.model.RefreshToken;
import com.beaverbyte.financial_tracker_application.model.Role;
import com.beaverbyte.financial_tracker_application.model.User;
import com.beaverbyte.financial_tracker_application.repository.UserRepository;
import com.beaverbyte.financial_tracker_application.security.CustomUserDetails;
import com.beaverbyte.financial_tracker_application.security.jwt.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final RoleService roleService;
	private final RefreshTokenService refreshTokenService;
	private final AuthenticationService authenticationService;

	public UserService(UserRepository userRepository, RoleService roleService, RefreshTokenService refreshTokenService,
			AuthenticationService authenticationService) {
		this.userRepository = userRepository;
		this.roleService = roleService;
		this.refreshTokenService = refreshTokenService;
		this.authenticationService = authenticationService;
	}

	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public User createUser(SignupRequest signUpRequest) {
		User user = UserMapper.toUser(signUpRequest);
		user.setPassword(authenticationService.encode(signUpRequest.getPassword()));
		Set<Role> roles = roleService.validateAgainstTable(signUpRequest.getRole());
		user.setRoles(roles);

		log.info("User created");
		return user;
	}

	public void save(User user) {
		log.info("User, {}, saved in database", user.getUsername());
		userRepository.save(user);
	}

	public boolean refreshTokenExistsForUser(Long userId) {
		return userRepository.findById(userId)
				.map(user -> {
					boolean userExists = refreshTokenService.findByUser(user).isPresent();
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
