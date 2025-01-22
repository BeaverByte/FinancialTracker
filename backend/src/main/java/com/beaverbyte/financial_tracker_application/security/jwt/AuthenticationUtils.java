package com.beaverbyte.financial_tracker_application.security.jwt;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtils {
	public boolean isAnonymous(Authentication authentication) {
		return authentication instanceof AnonymousAuthenticationToken;
	}

	public boolean hasActiveUser() {
		return !isAnonymous(getCurrentAuthentication());
	}

	public Authentication getCurrentAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public void setAuthentication(Authentication authentication) {
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
