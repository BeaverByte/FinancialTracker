package com.beaverbyte.financial_tracker_application;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.beaverbyte.financial_tracker_application.constants.ApiEndpoints;
import com.beaverbyte.financial_tracker_application.dto.request.LoginRequest;
import com.beaverbyte.financial_tracker_application.dto.request.SignupRequest;
import com.beaverbyte.financial_tracker_application.model.RoleType;
import com.beaverbyte.financial_tracker_application.model.Role;
import com.beaverbyte.financial_tracker_application.model.User;
import com.beaverbyte.financial_tracker_application.repository.RefreshTokenRepository;
import com.beaverbyte.financial_tracker_application.repository.RoleRepository;
import com.beaverbyte.financial_tracker_application.repository.UserRepository;
import com.beaverbyte.financial_tracker_application.security.jwt.JwtUtils;
import com.beaverbyte.financial_tracker_application.service.RoleService;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jakarta.servlet.http.HttpServletResponse;

class IntegrationTests extends AbstractIntegrationTest {

	@LocalServerPort
	private Integer port;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	RefreshTokenRepository refreshTokenRepository;

	@Autowired
	RoleService roleService;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Value("${JWT_COOKIE_NAME}")
	private String jwtCookieName;

	@Value("${JWT_REFRESH_COOKIE_NAME}")
	private String jwtRefreshCookieName;

	@BeforeAll
	static void beforeAll() {
		postgres.start();
	}

	@AfterAll
	static void afterAll() {
		postgres.stop();
	}

	@BeforeEach
	private void setUp() {
		RestAssured.baseURI = "http://localhost:" + port;

		sanitizeRepos();
		seedTestContainers();

		System.out.println("Database cleared before each test");
	}

	private void sanitizeRepos() {
		// Sanitizing repos

		refreshTokenRepository.deleteAll();
		userRepository.deleteAll();
		roleRepository.deleteAll();

		refreshTokenRepository.flush();
		userRepository.flush();
		roleRepository.flush();
	}

	private void seedTestContainers() {
		// Seeding TestContainers with roles
		Role roleUser = new Role(RoleType.ROLE_USER);
		roleRepository.save(roleUser);
		Role roleMod = new Role(RoleType.ROLE_MODERATOR);
		roleRepository.save(roleMod);
		Role roleAdmin = new Role(RoleType.ROLE_ADMIN);
		roleRepository.save(roleAdmin);
	}

	private Response signUp(SignupRequest signupRequest) {
		return given()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.and()
				.body(signupRequest)
				.when()
				.post(ApiEndpoints.AUTH + ApiEndpoints.SIGN_UP)
				.then()
				.extract().response();
	}

	private Response signIn(String username, String password) {
		return given()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.and()
				.body(new LoginRequest(username, password))
				.when()
				.post(ApiEndpoints.AUTH + ApiEndpoints.SIGN_IN)
				.then()
				.extract().response();
	}

	@Test
	void shouldAllowAuthorizedUserAccessToProtectedRoute() {
		SignupRequest signUpRequest = createSignupRequest("dumblikebricks",
				"dumbemail@gmail.com",
				"dumbpassword",
				RoleType.ROLE_MODERATOR);

		signUp(signUpRequest);

		Response signInResponse = signIn(signUpRequest.getUsername(), signUpRequest.getPassword());
		String sessionCookie = extractSessionCookie(signInResponse, jwtCookieName);

		Response response = sendGETRequestWithHeaders(
				ApiEndpoints.TEST + ApiEndpoints.MOD,
				Map.of("Cookie", sessionCookie) // Add the cookie here
		);
		Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
	}

	private String extractSessionCookie(Response response, String cookieName) {
		// Example: "SESSION=abc123; Path=/; HttpOnly; Secure"
		String setCookieHeader = response.getHeader("Set-Cookie");

		if (setCookieHeader != null) {
			// Extract the "SESSION" part (or your specific cookie name)
			return Arrays.stream(setCookieHeader.split(";"))
					.filter(cookie -> cookie.startsWith(cookieName + "="))
					.findFirst()
					.orElseThrow(() -> new IllegalStateException("SESSION cookie not found!"));
		}

		throw new IllegalStateException("Set-Cookie header not present in response!");
	}

	public Response sendGETRequestWithHeaders(String url, Map<String, String> headers) {
		RequestSpecification request = RestAssured.given();

		// Add headers to the request
		headers.forEach(request::header);

		return request.get(url);
	}

	@Test
	void shouldHaveZeroUsersInDatabaseAtStart() {
		long users = userRepository.count();
		assertEquals(0, users);
	}

	@Test
	void shouldHavePublicLinkAccessible() {
		given()
				.contentType(ContentType.JSON)
				.when()
				.get("/api/test/all")
				.then()
				.statusCode(200);
	}

	@Test
	void shouldAllowUserSignInWithCorrectDetails() {
		SignupRequest signUpRequest = createSignupRequest("dumblikebricks",
				"dumbemail@gmail.com",
				"dumbpassword",
				RoleType.ROLE_MODERATOR);

		signUp(signUpRequest);

		Response signInResponse = signIn(signUpRequest.getUsername(), signUpRequest.getPassword());
		Assertions.assertEquals(200, signInResponse.statusCode(), "Expecting OK");
	}

	@Test
	void shouldPreventUserSignInWithIncorrectDetails() {
		SignupRequest signUpRequest = createSignupRequest(
				"stupid",
				"stupid@gmail.com",
				"stupid",
				RoleType.ROLE_MODERATOR);

		signUp(signUpRequest);

		Response signInResponse = signIn(signUpRequest.getUsername() + "string to ruin username",
				signUpRequest.getPassword() + "string to ruin username");
		Assertions.assertEquals(HttpServletResponse.SC_UNAUTHORIZED, signInResponse.statusCode(),
				"Expecting Unauthorized");
	}

	@Test
	void shouldHaveCorrectRolesInDatabase() {
		List<Role> roles = roleRepository.findAll();
		int expectedRoles = 3;

		Assertions.assertNotNull(roleRepository);
		assertEquals(expectedRoles, roles.size());
		assertTrue(roles.stream().anyMatch(role -> role.getName().equals(RoleType.ROLE_USER)));
		assertTrue(roles.stream().anyMatch(role -> role.getName().equals(RoleType.ROLE_MODERATOR)));
		assertTrue(roles.stream().anyMatch(role -> role.getName().equals(RoleType.ROLE_ADMIN)));
	}

	@Test
	void shouldHaveRoleRepositoryInjected() {
		Assertions.assertNotNull(roleRepository, "RoleRepository should be injected.");
	}

	@Test
	void shouldAllowUserSignUp() {
		SignupRequest signUpRequest = new SignupRequest();
		signUpRequest.setUsername("dumbusername");
		signUpRequest.setEmail("dumbemailg@gmail.com");
		signUpRequest.setPassword("dumbpassword");

		Set<String> signUpRoles = Stream.of("user", "mod")
				.collect(Collectors.toCollection(HashSet::new));
		signUpRequest.setRole(signUpRoles);

		Response response = signUp(signUpRequest);

		Assertions.assertEquals(200, response.statusCode(), "Expecting OK");
	}

	SignupRequest createSignupRequest(String username, String email, String password, RoleType role) {
		SignupRequest signUpRequest = new SignupRequest();
		signUpRequest.setUsername(username);
		signUpRequest.setEmail(email);
		signUpRequest.setPassword(password);
		signUpRequest.setRole(getRolesForInput(role));

		return signUpRequest;
	}

	Set<String> getRolesForInput(RoleType role) {
		switch (role) {
			case ROLE_USER:
				return Stream.of("user")
						.collect(Collectors.toSet());
			case ROLE_MODERATOR:
				return Stream.of("user", "mod")
						.collect(Collectors.toSet());
			case ROLE_ADMIN:
				return Stream.of("admin")
						.collect(Collectors.toSet());
			default:
				throw new IllegalArgumentException("Invalid role input: " + role);
		}
	}

	@Test
	void shouldHaveUserInDatabaseAfterSignUp() {
		SignupRequest signUpRequest = createSignupRequest(
				"stupid",
				"stupid@gmail.com",
				"stupid",
				RoleType.ROLE_MODERATOR);

		Response response = signUp(signUpRequest);

		List<User> users = userRepository.findAll();

		Assertions.assertEquals(200, response.statusCode(), "Expecting OK for response");
		assertTrue(users.stream().anyMatch(user -> user.getUsername().contains(signUpRequest.getUsername())));
	}

	@Test
	void shouldHaveTokenHandledByAuthenticationManager() {
		SignupRequest signUpRequest = createSignupRequest(
				"salmon",
				"salmon@gmail.com",
				"salmon",
				RoleType.ROLE_MODERATOR);

		signUp(signUpRequest);

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				signUpRequest.getUsername(),
				signUpRequest.getPassword());

		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		assertTrue(authentication.isAuthenticated());
	}
}
