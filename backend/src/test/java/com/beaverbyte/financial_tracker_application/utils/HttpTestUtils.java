package com.beaverbyte.financial_tracker_application.utils;

import static io.restassured.RestAssured.given;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.beaverbyte.financial_tracker_application.dto.request.LoginRequest;
import com.beaverbyte.financial_tracker_application.dto.request.SignupRequest;
import com.beaverbyte.financial_tracker_application.model.RoleType;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class HttpTestUtils {
	public static String extractSessionCookie(Response signInResponse, String cookieName) {
		// Getting value from Header, like "SESSION=abc123; Path=/; HttpOnly; Secure"
		String setCookieHeader = signInResponse.getHeader("Set-Cookie");

		if (setCookieHeader != null) {
			// Extract part (or your specific cookie name)
			return Arrays.stream(setCookieHeader.split(";"))
					.filter(cookie -> cookie.startsWith(cookieName + "="))
					.findFirst()
					.orElseThrow(() -> new IllegalStateException("SESSION cookie not found!"));
		}

		throw new IllegalStateException("Set-Cookie header not present in response!");
	}

	public static Response signIn(String username, String password, String url) {
		return given()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.and()
				.body(new LoginRequest(username, password))
				.when()
				.post(url)
				.then()
				.extract().response();
	}

	public static Response signUp(SignupRequest signupRequest, String url) {
		return given()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.and()
				.body(signupRequest)
				.when()
				.post(url)
				.then()
				.extract().response();
	}

	public static SignupRequest createSignupRequest(String username, String email, String password, RoleType role) {
		SignupRequest signUpRequest = new SignupRequest();
		signUpRequest.setUsername(username);
		signUpRequest.setEmail(email);
		signUpRequest.setPassword(password);
		signUpRequest.setRole(getRolesForInput(role));

		return signUpRequest;
	}

	private static Set<String> getRolesForInput(RoleType role) {
		switch (role) {
			case ROLE_USER:
				return Set.of("user");
			case ROLE_MODERATOR:
				return Set.of("moderator", "user");
			case ROLE_ADMIN:
				return Set.of("admin");
			default:
				throw new IllegalArgumentException("Invalid role input: " + role);
		}
	}

	Set<String> createBadRole(String input) {
		return Stream.of(input)
				.collect(Collectors.toSet());
	}

	public static Response sendGETRequestWithHeaders(String url, Map<String, String> headers) {
		RequestSpecification request = RestAssured.given();

		// Add headers to the request
		headers.forEach(request::header);

		return request.get(url);
	}

	public static Response sendPOSTRequestWithHeaders(String url, Map<String, String> headers) {
		RequestSpecification request = RestAssured.given();

		headers.forEach(request::header);

		return request.post(url);
	}

	public static String signInAndGetSessionCookie(String username, String password, String signInUrl,
			String jwtCookieName) {
		Response signInResponse = signIn(username, password, signInUrl);
		return extractSessionCookie(signInResponse, jwtCookieName);
	}
}
