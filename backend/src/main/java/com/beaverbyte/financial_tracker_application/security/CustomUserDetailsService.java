package com.beaverbyte.financial_tracker_application.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beaverbyte.financial_tracker_application.model.User;
import com.beaverbyte.financial_tracker_application.repository.UserRepository;

/**
 * Returns {@link CustomUserDetails} using {@link UserRepository}'s query by
 * username.
 * 
 * <p>
 * Implements {@code UserDetailsService}, overriding its loadUserByUsername
 * method.
 * </p>
 * 
 * @see UserDetailsService
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
	final UserRepository userRepository;

	CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

		return CustomUserDetails.build(user);
	}
}