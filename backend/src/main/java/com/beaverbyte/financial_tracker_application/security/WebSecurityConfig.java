package com.beaverbyte.financial_tracker_application.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.beaverbyte.financial_tracker_application.security.jwt.AuthEntryPointJwt;
import com.beaverbyte.financial_tracker_application.security.jwt.AuthTokenFilter;
import com.beaverbyte.financial_tracker_application.security.jwt.JwtUtils;

/**
 * Configures security regarding authentication, http requests, etc.
 * In {@link #filterChain} the {@link #authenticationProvider} authenticates.
 * 
 */
@Configuration
@EnableMethodSecurity // AOP Security on methods
public class WebSecurityConfig {
	final CustomUserDetailsService userDetailsService;
	final AuthEntryPointJwt unauthorizedHandler;
	final JwtUtils jwtUtils;

	public WebSecurityConfig(CustomUserDetailsService userDetailsService, AuthEntryPointJwt unauthorizedHandler,
			JwtUtils jwtUtils) {
		this.userDetailsService = userDetailsService;
		this.unauthorizedHandler = unauthorizedHandler;
		this.jwtUtils = jwtUtils;
	}

	/**
	 * Factory method to create new {@link AuthTokenFilter}
	 */
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter(jwtUtils, userDetailsService);
	}

	/**
	 * An AuthenticationManager delegates to this with an authenticate() method.
	 * 
	 * Validates user provided username (with {@link #userDetailsService}) and
	 * password against hashed password in database using set
	 * {@link #passwordEncoder}. Adds to SecurityContext upon success.
	 * 
	 */
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		// Service used here to inject user Details when they're fired
		authProvider.setUserDetailsService(userDetailsService);
		// PasswordEncoder set, otherwise will be plain text
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	// Delegates to one or more AuthenticationProvider implementations for
	// authentication
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Security that applies to HTTP requests
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll()
						.requestMatchers("/api/test/**").permitAll()
						.anyRequest().authenticated());

		http.authenticationProvider(authenticationProvider());

		// Adding to filter before to ensure Jwt Filter for authenticating users
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}