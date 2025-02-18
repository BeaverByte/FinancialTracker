package com.beaverbyte.financial_tracker_application.security.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.beaverbyte.financial_tracker_application.security.CustomUserDetailsService;

/**
 * Filters token, validating the token. On success, an user is returned, and set
 * as the principal in Security Context.
 * 
 */
public class AuthTokenFilter extends OncePerRequestFilter {
	private final JwtUtils jwtUtils;
	private final CustomUserDetailsService userDetailsService;

	private static final Logger log = LoggerFactory.getLogger(AuthTokenFilter.class);

	public AuthTokenFilter(JwtUtils jwtUtils, CustomUserDetailsService userDetailsService) {
		this.jwtUtils = jwtUtils;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = parseJwt(request);
			if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
				String username = jwtUtils.getUserNameFromJwtToken(jwt);

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails,
						null,
						userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			log.error("Cannot set user authentication: {}", e.getMessage());
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * Checks request's header for "Authorization" to extract out JWT token
	 * 
	 * @return JWT token if present and validated, otherwise null
	 */
	private String parseJwt(HttpServletRequest request) {
		return jwtUtils.getJwtValueFromCookies(request);
	}
}