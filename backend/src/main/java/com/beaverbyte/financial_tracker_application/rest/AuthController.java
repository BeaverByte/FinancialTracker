package com.beaverbyte.financial_tracker_application.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beaverbyte.financial_tracker_application.dto.api.request.LoginRequest;
import com.beaverbyte.financial_tracker_application.dto.api.request.SignupRequest;
import com.beaverbyte.financial_tracker_application.dto.api.response.LoginResponse;
import com.beaverbyte.financial_tracker_application.dto.api.response.JwtResponse;
import com.beaverbyte.financial_tracker_application.dto.api.response.MessageResponse;
import com.beaverbyte.financial_tracker_application.dto.api.response.RefreshTokenResponse;
import com.beaverbyte.financial_tracker_application.dto.api.response.UserInfoResponse;
import com.beaverbyte.financial_tracker_application.entity.User;
import com.beaverbyte.financial_tracker_application.repository.RoleRepository;
import com.beaverbyte.financial_tracker_application.repository.UserRepository;
import com.beaverbyte.financial_tracker_application.security.CustomUserDetails;
import com.beaverbyte.financial_tracker_application.security.jwt.JwtUtils;
import com.beaverbyte.financial_tracker_application.service.RefreshTokenService;
import com.beaverbyte.financial_tracker_application.service.RoleService;
import com.beaverbyte.financial_tracker_application.service.UserInfoResponseService;
import com.beaverbyte.financial_tracker_application.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserService userService;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  RoleService roleService;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  RefreshTokenService refreshTokenService;

  @Autowired
  UserInfoResponseService userInfoResponseService;

  /**
   * App consumers sign in and have their request authenticated
   * 
   * @param loginRequest
   * @return
   */
  @PostMapping("/signin")
  public ResponseEntity<UserInfoResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
	LoginResponse loginResponse = userService.login(loginRequest);
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
	Object principalUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

	if (isAnonymous(principalUser)) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Logout cannot occur without user already logged in"));
	}

	Long userId = ((CustomUserDetails) principalUser).getId();
	refreshTokenService.deleteByUserId(userId);

	JwtResponse logoutResponse = userService.logoutUser();

	return ResponseEntity.ok()
		.header(HttpHeaders.SET_COOKIE, logoutResponse.jwtCookie().toString())
		.header(HttpHeaders.SET_COOKIE, logoutResponse.jwtRefreshCookie().toString())
		.body(logoutResponse.messageResponse());
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshToken(HttpServletRequest request) {
	RefreshTokenResponse refreshTokenResponse = userService.refreshToken(request);

	return ResponseEntity.ok()
		.header(HttpHeaders.SET_COOKIE, refreshTokenResponse.jwtCookie().toString())
		.body(refreshTokenResponse.messageResponse());
  }

  public boolean isAnonymous(Object principalUser) {
	return principalUser.toString().equals("anonymousUser");
  }
}