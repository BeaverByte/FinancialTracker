package com.beaverbyte.financial_tracker_application.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beaverbyte.financial_tracker_application.entity.User;
import com.beaverbyte.financial_tracker_application.repository.UserRepository;

/**
 * Returns {@link CustomUserDetails} using {@link UserRepository}'s query by username. 
 * 
 * <p>
 * Implements {@code UserDetailsService}, overriding its loadUserByUsername method.
 * </p>
 * @see UserDetailsService
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return CustomUserDetails.build(user);
  }

  // public UserDetails getUserDetailsPrincipal() {
  //   // Check if SecurityContext exists
  //   if (SecurityContextHolder.getContext() == null) {
  //       throw new IllegalStateException("SecurityContext is not available.");
  //   }
  //   // Check if Authentication exists
  //   if (SecurityContextHolder.getContext().getAuthentication() == null) {
  //       throw new IllegalStateException("No authentication data is available in the SecurityContext.");
  //   }
  //   // Retrieve the principal and check its type
  //   Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  //   if (!(principal instanceof UserDetails)) {
  //       throw new IllegalStateException("Principal is not an instance of UserDetails.");
  //   }

  //   return (UserDetails) principal;
  // }

}