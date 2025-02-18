package com.beaverbyte.financial_tracker_application.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.beaverbyte.financial_tracker_application.constants.ApiEndpoints;
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
				.cors(Customizer.withDefaults())
				.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll()
						.requestMatchers("/api/test/**").permitAll()
						.requestMatchers("/v3/api-docs/**",
								"/swagger-ui/**", "/swagger-ui.html")
						.permitAll()
						.anyRequest().authenticated());

		// Adding to filter before to ensure Jwt Filter for authenticating users
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration configuration = new CorsConfiguration();
		// Specify the allowed origin, e.g., your frontend URL
		configuration.setAllowedOrigins(List.of(ApiEndpoints.FRONTEND_URL)); // Frontend URL here
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
		configuration.setExposedHeaders(List.of("Authorization")); // Optional: expose specific headers
		configuration.setAllowCredentials(true); // Allow credentials (cookies)

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration); // Apply to all endpoints
		return new CorsFilter(source);
	}

}