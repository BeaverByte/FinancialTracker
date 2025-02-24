package com.beaverbyte.financial_tracker_application.service;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.dto.request.SignupRequest;
import com.beaverbyte.financial_tracker_application.mapper.UserMapper;
import com.beaverbyte.financial_tracker_application.model.Role;
import com.beaverbyte.financial_tracker_application.model.User;
import com.beaverbyte.financial_tracker_application.repository.UserRepository;

@Service
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final RoleService roleService;
	private final AuthenticationService authenticationService;

	public UserService(UserRepository userRepository, RoleService roleService,
			AuthenticationService authenticationService) {
		this.userRepository = userRepository;
		this.roleService = roleService;
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
		log.info("Validating signup request's roles");
		Set<Role> roles = roleService.validate(signUpRequest.getRole());
		user.setRoles(roles);

		log.info("User created");
		return user;
	}

	public void save(User user) {
		log.info("User, {}, saved in database", user.getUsername());
		userRepository.save(user);
	}

}
