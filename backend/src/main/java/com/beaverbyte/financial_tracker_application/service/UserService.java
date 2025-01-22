package com.beaverbyte.financial_tracker_application.service;

import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.dto.api.request.LoginRequest;
import com.beaverbyte.financial_tracker_application.dto.api.request.SignupRequest;
import com.beaverbyte.financial_tracker_application.dto.api.response.MessageResponse;
import com.beaverbyte.financial_tracker_application.dto.api.response.UserInfoResponse;
import com.beaverbyte.financial_tracker_application.entity.RefreshToken;
import com.beaverbyte.financial_tracker_application.entity.Role;
import com.beaverbyte.financial_tracker_application.entity.User;
import com.beaverbyte.financial_tracker_application.exception.TokenRefreshException;
import com.beaverbyte.financial_tracker_application.mapper.UserMapper;
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

	public boolean existsByUsername(String username){
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
	public ResponseEntity<?> login(LoginRequest loginRequest) {
		Authentication authentication = authenticate(loginRequest);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

		UserInfoResponse userInfoResponse = userInfoResponseService.createUserInfoResponse(userDetails);
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
		ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
			.header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
			.body(userInfoResponse);
	}

	public Authentication authenticate(LoginRequest loginRequest) {
		return authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
	}

	public ResponseEntity<?> logoutUser() {
		Object principalUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!principalUser.toString().equals("anonymousUser")) {
			Long userId = ((CustomUserDetails) principalUser).getId();
			refreshTokenService.deleteByUserId(userId);
		}

		ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
		ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
			.header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
			.body(new MessageResponse("You've been signed out!"));
	}

	public ResponseEntity<?> refreshtoken(HttpServletRequest request) {
		String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

		if (refreshToken != null && !refreshToken.isEmpty()) {
		return refreshTokenService.findByToken(refreshToken)
			.map(refreshTokenService::verifyExpiration)
			.map(RefreshToken::getUser)
			.map(user -> {
				ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

				return ResponseEntity.ok()
					.header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
					.body(new MessageResponse("Token is refreshed successfully!"));
			})
			.orElseThrow(() -> new TokenRefreshException(refreshToken,
				"Refresh token is not in database!"));
		}

		return ResponseEntity.badRequest().body(new MessageResponse("Refresh Token is empty!"));
	}

}
