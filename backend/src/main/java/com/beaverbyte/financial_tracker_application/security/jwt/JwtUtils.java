package com.beaverbyte.financial_tracker_application.security.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.beaverbyte.financial_tracker_application.constants.ApiEndpoints;
import com.beaverbyte.financial_tracker_application.security.UserBase;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${JWT_SECRET}")
	private String jwtSecret;

	@Value("${JWT_EXPIRATION_MS}")
	private int jwtExpirationMs;

	@Value("${JWT_COOKIE_NAME}")
	private String cookieName;

	@Value("${JWT_REFRESH_COOKIE_NAME}")
	private String refreshCookieName;

	public ResponseCookie generateJwtCookie(UserBase user) {
		String jwt = generateTokenFromUsername(user.getUsername());
		return generateCookie(cookieName, jwt, ApiEndpoints.BASE);
	}

	public ResponseCookie generateRefreshJwtCookie(String refreshTokenValue) {
		return generateCookie(refreshCookieName, refreshTokenValue, ApiEndpoints.AUTH_REFRESH_TOKEN_URL);
	}

	public String getJwtValueFromCookies(HttpServletRequest request) {
		return getCookieValueByName(request, cookieName);
	}

	public String getJwtRefreshValueFromCookies(HttpServletRequest request) {
		return getCookieValueByName(request, refreshCookieName);
	}

	public ResponseCookie getCleanJwtCookie() {
		return ResponseCookie.from(cookieName).path(ApiEndpoints.BASE).build();
	}

	public ResponseCookie getCleanJwtRefreshCookie() {
		return ResponseCookie.from(refreshCookieName).path(ApiEndpoints.AUTH_REFRESH_TOKEN_URL).build();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser()
				.verifyWith(key())
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}

	private SecretKey key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().verifyWith(key()).build().parse(authToken);
			return true;
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

	public String generateTokenFromUsername(String username) {
		return Jwts.builder()
				.subject(username)
				.issuedAt(new Date())
				.expiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(key(), Jwts.SIG.HS256)
				.compact();
	}

	private ResponseCookie generateCookie(String name, String value, String path) {
		return ResponseCookie
				.from(name, value)
				.path(path)
				.maxAge((long) 24 * 60 * 60)
				.httpOnly(true).build();
	}

	private String getCookieValueByName(HttpServletRequest request, String name) {
		Cookie cookie = WebUtils.getCookie(request, name);
		if (cookie != null) {
			return cookie.getValue();
		} else {
			return null;
		}
	}

}