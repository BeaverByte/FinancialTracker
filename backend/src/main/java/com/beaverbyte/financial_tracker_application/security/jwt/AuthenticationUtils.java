package com.beaverbyte.financial_tracker_application.security.jwt;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.beaverbyte.financial_tracker_application.security.CustomUserDetails;

@Component
public class AuthenticationUtils {
	public boolean isAnonymous(Authentication authentication) {
		return authentication instanceof AnonymousAuthenticationToken;
	}

	public boolean hasActiveUser() {
		return !isAnonymous(getCurrentAuthentication());
	}

	/**
	 * Retrieves the current authentication object from the security context.
	 * 
	 * @return the Authentication object representing the current user's
	 *         authentication details
	 */
	public Authentication getCurrentAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public void setAuthentication(Authentication authentication) {
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	public static CustomUserDetails getCustomUserDetails(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		if (principal instanceof CustomUserDetails customUserDetails) {
			return customUserDetails;
		}
		throw new IllegalStateException("Authentication principal is not of the expected type.");
	}
}
