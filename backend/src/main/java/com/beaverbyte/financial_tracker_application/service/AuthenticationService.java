package com.beaverbyte.financial_tracker_application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.dto.request.LoginRequest;
import com.beaverbyte.financial_tracker_application.dto.response.JwtResponse;
import com.beaverbyte.financial_tracker_application.dto.response.LoginResponse;
import com.beaverbyte.financial_tracker_application.dto.response.MessageResponse;
import com.beaverbyte.financial_tracker_application.dto.response.RefreshTokenResponse;
import com.beaverbyte.financial_tracker_application.dto.response.UserInfoResponse;
import com.beaverbyte.financial_tracker_application.exception.TokenRefreshException;
import com.beaverbyte.financial_tracker_application.mapper.AuthenticationMapper;
import com.beaverbyte.financial_tracker_application.model.RefreshToken;
import com.beaverbyte.financial_tracker_application.security.CustomUserDetails;
import com.beaverbyte.financial_tracker_application.security.jwt.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthenticationService {
	private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

	private final PasswordEncoder encoder;
	private final JwtUtils jwtUtils;
	private final AuthenticationManager authenticationManager;
	private final UserInfoResponseService userInfoResponseService;
	private final RefreshTokenService refreshTokenService;
	private final AuthenticationMapper authenticationMapper;

	public AuthenticationService(PasswordEncoder encoder, JwtUtils jwtUtils,
			AuthenticationManager authenticationManager, UserInfoResponseService userInfoResponseService,
			RefreshTokenService refreshTokenService, AuthenticationMapper authenticationMapper) {
		this.encoder = encoder;
		this.jwtUtils = jwtUtils;
		this.authenticationManager = authenticationManager;
		this.userInfoResponseService = userInfoResponseService;
		this.refreshTokenService = refreshTokenService;
		this.authenticationMapper = authenticationMapper;
	}

	public UsernamePasswordAuthenticationToken createAuthenticationToken(UserDetails userDetails,
			HttpServletRequest request) {
		return authenticationMapper.toAuthenticationToken(userDetails, request);
	}

	public Authentication authenticate(LoginRequest loginRequest) {
		return authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
	}

	public String encode(String password) {
		log.info("Encoding password");
		return encoder.encode(password);
	}

	public LoginResponse login(CustomUserDetails userDetails) {
		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

		UserInfoResponse userInfoResponse = userInfoResponseService.createUserInfoResponse(userDetails);
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
		ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

		return new LoginResponse(userInfoResponse, jwtRefreshCookie, jwtCookie);
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