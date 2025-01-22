package com.beaverbyte.financial_tracker_application.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beaverbyte.financial_tracker_application.dto.response.UserInfoResponse;
import com.beaverbyte.financial_tracker_application.dto.request.LoginRequest;
import com.beaverbyte.financial_tracker_application.dto.request.SignupRequest;
import com.beaverbyte.financial_tracker_application.dto.response.LoginResponse;
import com.beaverbyte.financial_tracker_application.dto.response.JwtResponse;
import com.beaverbyte.financial_tracker_application.dto.response.MessageResponse;
import com.beaverbyte.financial_tracker_application.dto.response.RefreshTokenResponse;
import com.beaverbyte.financial_tracker_application.entity.User;
import com.beaverbyte.financial_tracker_application.security.CustomUserDetails;
import com.beaverbyte.financial_tracker_application.security.jwt.AuthenticationUtils;
import com.beaverbyte.financial_tracker_application.service.RefreshTokenService;
import com.beaverbyte.financial_tracker_application.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final UserService userService;
	private final RefreshTokenService refreshTokenService;
	private final AuthenticationUtils authenticationUtils;

	public AuthController(UserService userService, RefreshTokenService refreshTokenService,
			AuthenticationUtils authenticationUtils) {
		this.userService = userService;
		this.refreshTokenService = refreshTokenService;
		this.authenticationUtils = authenticationUtils;
	}

	/**
	 * App consumers sign in and have their request authenticated
	 * 
	 * @param loginRequest
	 * @return
	 */
	@PostMapping("/signin")
	public ResponseEntity<UserInfoResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = userService.authenticate(loginRequest);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		LoginResponse loginResponse = userService.login(userDetails);

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, loginResponse.jwtCookie().toString())
				.header(HttpHeaders.SET_COOKIE, loginResponse.jwtRefreshCookie().toString())
				.body(loginResponse.userInfoResponse());
	}

	@PostMapping("/signup")
	public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userService.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}
		if (userService.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		User user = userService.createUser(signUpRequest);
		userService.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/signout")
	public ResponseEntity<MessageResponse> logoutUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authenticationUtils.isAnonymous(authentication)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
					new MessageResponse("User must be logged in to perform logout"));
		}

		Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
		refreshTokenService.deleteByUserId(userId);

		JwtResponse logoutResponse = userService.logoutUser();

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, logoutResponse.jwtCookie().toString())
				.header(HttpHeaders.SET_COOKIE, logoutResponse.jwtRefreshCookie().toString())
				.body(logoutResponse.messageResponse());
	}

	@PostMapping("/refreshtoken")
	public ResponseEntity<MessageResponse> refreshToken(HttpServletRequest request) {
		RefreshTokenResponse refreshTokenResponse = userService.refreshToken(request);

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, refreshTokenResponse.jwtCookie().toString())
				.body(refreshTokenResponse.messageResponse());
	}
}