package com.beaverbyte.financial_tracker_application.service;

import java.util.Set;

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

	private final UserRepository userRepository;
	private final RoleService roleService;
	private final PasswordEncoder encoder;
	private final JwtUtils jwtUtils;
	private final AuthenticationManager authenticationManager;
	private final UserInfoResponseService userInfoResponseService;
	private final RefreshTokenService refreshTokenService;

	public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder encoder,
			JwtUtils jwtUtils, AuthenticationManager authenticationManager,
			UserInfoResponseService userInfoResponseService, RefreshTokenService refreshTokenService) {
		this.userRepository = userRepository;
		this.roleService = roleService;
		this.encoder = encoder;
		this.jwtUtils = jwtUtils;
		this.authenticationManager = authenticationManager;
		this.userInfoResponseService = userInfoResponseService;
		this.refreshTokenService = refreshTokenService;
	}

	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public User createUser(SignupRequest signUpRequest) {
		User user = UserMapper.toUser(signUpRequest);
		user.setPassword(encoder.encode(signUpRequest.getPassword()));
		Set<Role> roles = roleService.validateAgainstTable(signUpRequest.getRole());
		user.setRoles(roles);
		return user;
	}

	public void save(User user) {
		userRepository.save(user);
	}

	public LoginResponse login(CustomUserDetails userDetails) {
		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

		UserInfoResponse userInfoResponse = userInfoResponseService.createUserInfoResponse(userDetails);
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
		ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

		return new LoginResponse(userInfoResponse, jwtRefreshCookie, jwtCookie);
	}

	public Authentication authenticate(LoginRequest loginRequest) {
		return authenticationManager.authenticate(
				// new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
				// loginRequest.getPassword()));
				new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
	}

	public JwtResponse logoutUser() {
		ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
		ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

		return new JwtResponse(new MessageResponse("You've been signed out!"), jwtCookie, jwtRefreshCookie);
	}

	public RefreshTokenResponse refreshToken(HttpServletRequest request) {
		String refreshJwt = jwtUtils.getJwtRefreshFromCookies(request);

		if (refreshJwt == null || refreshJwt.isEmpty()) {
			throw new TokenRefreshException(refreshJwt, "Refresh token is empty");
		}

		RefreshToken refreshToken = refreshTokenService.findByToken(refreshJwt)
				.orElseThrow(() -> new TokenRefreshException(refreshJwt, "Refresh token is not found in database"));

		refreshTokenService.verifyExpiration(refreshToken);

		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(refreshToken.getUser());

		return new RefreshTokenResponse(new MessageResponse("Refresh Token is refreshed successfully!"), jwtCookie);
	}
}
