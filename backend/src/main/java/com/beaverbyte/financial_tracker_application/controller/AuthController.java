package com.beaverbyte.financial_tracker_application.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beaverbyte.financial_tracker_application.dto.response.UserInfoResponse;
import com.beaverbyte.financial_tracker_application.exception.UserNotLoggedInException;
import com.beaverbyte.financial_tracker_application.exception.SignupException;
import com.beaverbyte.financial_tracker_application.model.User;
import com.beaverbyte.financial_tracker_application.constants.ApiEndpoints;
import com.beaverbyte.financial_tracker_application.dto.request.LoginRequest;
import com.beaverbyte.financial_tracker_application.dto.request.SignupRequest;
import com.beaverbyte.financial_tracker_application.dto.response.LoginResponse;
import com.beaverbyte.financial_tracker_application.dto.response.JwtResponse;
import com.beaverbyte.financial_tracker_application.dto.response.MessageResponse;
import com.beaverbyte.financial_tracker_application.dto.response.RefreshTokenResponse;
import com.beaverbyte.financial_tracker_application.security.CustomUserDetails;
import com.beaverbyte.financial_tracker_application.security.jwt.AuthenticationUtils;
import com.beaverbyte.financial_tracker_application.service.AuthenticationService;
import com.beaverbyte.financial_tracker_application.service.RefreshTokenService;
import com.beaverbyte.financial_tracker_application.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiEndpoints.AUTH)
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	private final UserService userService;
	private final AuthenticationService authenticationService;
	private final RefreshTokenService refreshTokenService;

	public AuthController(UserService userService, AuthenticationService authenticationService,
			RefreshTokenService refreshTokenService) {
		this.userService = userService;
		this.authenticationService = authenticationService;
		this.refreshTokenService = refreshTokenService;
	}

	/**
	 * App consumers sign in and have their request authenticated
	 * 
	 * @param loginRequest
	 * @return
	 */
	@PostMapping(ApiEndpoints.SIGN_IN)
	public ResponseEntity<UserInfoResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationService.authenticate(loginRequest);
		AuthenticationUtils.setAuthentication(authentication);
		CustomUserDetails userDetails = AuthenticationUtils.getCustomUserDetails(authentication);

		if (userService.refreshTokenExistsForUser(userDetails.getId())) {
			log.info("Refresh Token exists for given user ID, deleting token");
			refreshTokenService.deleteByUserId(userDetails.getId());
		}

		LoginResponse loginResponse = authenticationService.login(userDetails);

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, loginResponse.jwtCookie().toString())
				.header(HttpHeaders.SET_COOKIE, loginResponse.jwtRefreshCookie().toString())
				.body(loginResponse.userInfoResponse());
	}

	@PostMapping(ApiEndpoints.SIGN_UP)
	public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userService.existsByUsername(signUpRequest.getUsername())) {
			log.error("Username in signup already used");
			throw new SignupException("Username already in use!");
		}
		if (userService.existsByEmail(signUpRequest.getEmail())) {
			log.error("Email in signup already used");
			throw new SignupException("Email already in use!");
		}

		User user = userService.createUser(signUpRequest);

		userService.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping(ApiEndpoints.SIGN_OUT)
	public ResponseEntity<MessageResponse> logoutUser() {
		if (!AuthenticationUtils.hasActiveUser()) {
			throw new UserNotLoggedInException("Action requires active session");
		}

		Authentication authentication = AuthenticationUtils.getCurrentAuthentication();
		CustomUserDetails userDetails = AuthenticationUtils.getCustomUserDetails(authentication);

		if (userService.refreshTokenExistsForUser(userDetails.getId())) {
			log.info("Refresh Token exists for given user ID, deleting token");
			refreshTokenService.deleteByUserId(userDetails.getId());
		}

		JwtResponse logoutResponse = authenticationService.logoutUser();

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, logoutResponse.jwtCookie().toString())
				.header(HttpHeaders.SET_COOKIE, logoutResponse.jwtRefreshCookie().toString())
				.body(logoutResponse.messageResponse());
	}

	@PostMapping(ApiEndpoints.REFRESH_TOKEN)
	public ResponseEntity<MessageResponse> refreshToken(HttpServletRequest request) {
		RefreshTokenResponse refreshTokenResponse = authenticationService.refreshToken(request);

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, refreshTokenResponse.jwtCookie().toString())
				.body(refreshTokenResponse.messageResponse());
	}
}