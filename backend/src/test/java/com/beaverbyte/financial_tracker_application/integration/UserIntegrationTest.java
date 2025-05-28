package com.beaverbyte.financial_tracker_application.integration;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.beaverbyte.financial_tracker_application.constants.ApiEndpoints;
import com.beaverbyte.financial_tracker_application.dto.request.SignupRequest;
import com.beaverbyte.financial_tracker_application.model.RoleType;
import com.beaverbyte.financial_tracker_application.model.Role;
import com.beaverbyte.financial_tracker_application.model.User;
import com.beaverbyte.financial_tracker_application.repository.RefreshTokenRepository;
import com.beaverbyte.financial_tracker_application.repository.RoleRepository;
import com.beaverbyte.financial_tracker_application.repository.UserRepository;
import com.beaverbyte.financial_tracker_application.security.CustomUserDetailsService;
import com.beaverbyte.financial_tracker_application.security.jwt.AuthenticationUtils;
import com.beaverbyte.financial_tracker_application.service.RefreshTokenService;
import com.beaverbyte.financial_tracker_application.service.RoleService;
import com.beaverbyte.financial_tracker_application.utils.HttpTestUtils;
import com.beaverbyte.financial_tracker_application.utils.JpaTestUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.servlet.http.HttpServletResponse;
import net.datafaker.Faker;

class UserIntegrationTest extends AbstractIntegrationTest {

	@LocalServerPort
	private Integer port;

	@Autowired
	AuthenticationManager authenticationManager;

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
	CustomUserDetailsService customUserDetailsService;

	@Autowired
	RefreshTokenService refreshTokenService;

	@Value("${JWT_COOKIE_NAME}")
	private String jwtCookieName;

	@Value("${JWT_REFRESH_COOKIE_NAME}")
	private String jwtRefreshCookieName;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost:" + port;

		sanitizeRepos();
		seedTestContainers();
		SecurityContextHolder.clearContext();

		System.out.println("Database cleared before each test");
	}

	private void sanitizeRepos() {
		// Sanitizing repos

		JpaTestUtils.clearRepository(refreshTokenRepository);
		JpaTestUtils.clearRepository(userRepository);
		JpaTestUtils.clearRepository(roleRepository);
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

	private Faker faker = new Faker();

	@Test
	void shouldAllowAuthorizedUserAccessToProtectedRoute() {
		SignupRequest signUpRequest = HttpTestUtils.createSignupRequest(
				faker.internet().username(),
				faker.internet().emailAddress(),
				faker.internet().password(),
				RoleType.ROLE_USER);

		HttpTestUtils.signUp(signUpRequest, ApiEndpoints.AUTH_SIGN_UP_URL);

		String sessionCookie = HttpTestUtils.signInAndGetSessionCookie(signUpRequest.getUsername(),
				signUpRequest.getPassword(),
				ApiEndpoints.AUTH_SIGN_IN_URL,
				jwtCookieName);

		Response response = HttpTestUtils.sendGETRequestWithHeaders(
				ApiEndpoints.TEST_USER_URL,
				Map.of("Cookie", sessionCookie));

		Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
	}

	@Test
	void shouldNotAllowUnAuthorizedUserAccessToAdminRoute() {
		SignupRequest signUpRequest = HttpTestUtils.createSignupRequest(
				faker.internet().username(),
				faker.internet().emailAddress(),
				faker.internet().password(),
				RoleType.ROLE_USER);

		HttpTestUtils.signUp(signUpRequest, ApiEndpoints.AUTH_SIGN_UP_URL);

		String sessionCookie = HttpTestUtils.signInAndGetSessionCookie(signUpRequest.getUsername(),
				signUpRequest.getPassword(),
				ApiEndpoints.AUTH_SIGN_IN_URL,
				jwtCookieName);

		Response response = HttpTestUtils.sendGETRequestWithHeaders(
				ApiEndpoints.TEST_ADMIN_URL,
				Map.of("Cookie", sessionCookie));

		Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.statusCode());
	}

	@Test
	void shouldAllowAuthorizedModeratorAccessToModeratorRoute() {
		SignupRequest signUpRequest = HttpTestUtils.createSignupRequest(
				faker.internet().username(),
				faker.internet().emailAddress(),
				faker.internet().password(),
				RoleType.ROLE_MODERATOR);

		HttpTestUtils.signUp(signUpRequest, ApiEndpoints.AUTH_SIGN_UP_URL);

		String sessionCookie = HttpTestUtils.signInAndGetSessionCookie(signUpRequest.getUsername(),
				signUpRequest.getPassword(),
				ApiEndpoints.AUTH_SIGN_IN_URL,
				jwtCookieName);

		Response response = HttpTestUtils.sendGETRequestWithHeaders(
				ApiEndpoints.TEST_MOD_URL,
				Map.of("Cookie", sessionCookie));

		Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
	}

	@Test
	void shouldNotAllowUnAuthorizedModeratorAccessToAdminRoute() {
		SignupRequest signUpRequest = HttpTestUtils.createSignupRequest(
				faker.internet().username(),
				faker.internet().emailAddress(),
				faker.internet().password(),
				RoleType.ROLE_MODERATOR);

		HttpTestUtils.signUp(signUpRequest, ApiEndpoints.AUTH_SIGN_UP_URL);

		String sessionCookie = HttpTestUtils.signInAndGetSessionCookie(signUpRequest.getUsername(),
				signUpRequest.getPassword(),
				ApiEndpoints.AUTH_SIGN_IN_URL,
				jwtCookieName);

		Response response = HttpTestUtils.sendGETRequestWithHeaders(
				ApiEndpoints.TEST_ADMIN_URL,
				Map.of("Cookie", sessionCookie));

		Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.statusCode());
	}

	@Test
	void shouldAllowAuthorizedAdminAccessToModeratorRoute() {
		SignupRequest signUpRequest = HttpTestUtils.createSignupRequest(
				faker.internet().username(),
				faker.internet().emailAddress(),
				faker.internet().password(),
				RoleType.ROLE_ADMIN);

		HttpTestUtils.signUp(signUpRequest, ApiEndpoints.AUTH_SIGN_UP_URL);

		String sessionCookie = HttpTestUtils.signInAndGetSessionCookie(signUpRequest.getUsername(),
				signUpRequest.getPassword(),
				ApiEndpoints.AUTH_SIGN_IN_URL,
				jwtCookieName);

		Response response = HttpTestUtils.sendGETRequestWithHeaders(
				ApiEndpoints.TEST_ADMIN_URL,
				Map.of("Cookie", sessionCookie));

		Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
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
		SignupRequest signUpRequest = HttpTestUtils.createSignupRequest(
				faker.internet().username(),
				faker.internet().emailAddress(),
				faker.internet().password(),
				RoleType.ROLE_MODERATOR);

		HttpTestUtils.signUp(signUpRequest, ApiEndpoints.AUTH_SIGN_UP_URL);

		Response signInResponse = HttpTestUtils.signIn(signUpRequest.getUsername(), signUpRequest.getPassword(),
				ApiEndpoints.AUTH_SIGN_IN_URL);
		Assertions.assertEquals(200, signInResponse.statusCode(), "Expecting OK");
	}

	@Test
	void shouldAllowUserSignOut() {
		SignupRequest signUpRequest = HttpTestUtils.createSignupRequest(
				faker.internet().username(),
				faker.internet().emailAddress(),
				faker.internet().password(),
				RoleType.ROLE_MODERATOR);
		HttpTestUtils.signUp(signUpRequest, ApiEndpoints.AUTH_SIGN_UP_URL);

		String sessionCookie = HttpTestUtils.signInAndGetSessionCookie(signUpRequest.getUsername(),
				signUpRequest.getPassword(),
				ApiEndpoints.AUTH_SIGN_IN_URL,
				jwtCookieName);

		Response signOutResponse = HttpTestUtils.sendPOSTRequestWithHeaders(
				ApiEndpoints.AUTH_SIGN_OUT_URL,
				Map.of("Cookie", sessionCookie));

		Assertions.assertEquals(HttpStatus.OK.value(), signOutResponse.statusCode());
		Assertions.assertEquals(200, signOutResponse.statusCode(), "Expecting User Sign Out");
	}

	@Test
	void shouldPreventUserSignInWithIncorrectDetails() {
		SignupRequest signUpRequest = HttpTestUtils.createSignupRequest(
				faker.internet().username(),
				faker.internet().emailAddress(),
				faker.internet().password(),
				RoleType.ROLE_MODERATOR);

		HttpTestUtils.signUp(signUpRequest, ApiEndpoints.AUTH_SIGN_UP_URL);

		String ruiner = "string to ruin credential";
		String badUsername = signUpRequest.getUsername() + ruiner;
		String badPassword = signUpRequest.getPassword() + ruiner;

		Response signInResponse = HttpTestUtils.signIn(badUsername, badPassword,
				ApiEndpoints.AUTH_SIGN_IN_URL);

		Assertions.assertEquals(HttpServletResponse.SC_UNAUTHORIZED, signInResponse.statusCode(),
				"Expecting Unauthorized");
	}

	Set<String> createBadRole(String input) {
		return Stream.of(input)
				.collect(Collectors.toSet());
	}

	@Test
	void shouldPreventUserSignUpWithInvalidRole() {
		SignupRequest signUpRequest = new SignupRequest(
				faker.internet().username(),
				faker.internet().emailAddress(),
				createBadRole("Yuck"),
				faker.internet().password());

		Response response = HttpTestUtils.signUp(signUpRequest, ApiEndpoints.AUTH_SIGN_UP_URL);

		Assertions.assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.statusCode(),
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
		SignupRequest signUpRequest = HttpTestUtils.createSignupRequest(
				faker.internet().username(),
				faker.internet().emailAddress(),
				faker.internet().password(),
				RoleType.ROLE_MODERATOR);

		Response response = HttpTestUtils.signUp(signUpRequest, ApiEndpoints.AUTH_SIGN_UP_URL);

		Assertions.assertEquals(200, response.statusCode(), "Expecting OK");
	}

	@Test
	void shouldHaveUserInDatabaseAfterSignUp() {
		SignupRequest signUpRequest = HttpTestUtils.createSignupRequest(
				faker.internet().username(),
				faker.internet().emailAddress(),
				faker.internet().password(),
				RoleType.ROLE_MODERATOR);

		Response response = HttpTestUtils.signUp(signUpRequest, ApiEndpoints.AUTH_SIGN_UP_URL);

		List<User> users = userRepository.findAll();

		Assertions.assertEquals(200, response.statusCode(), "Expecting OK for response");
		assertTrue(users.stream().anyMatch(user -> user.getUsername().contains(signUpRequest.getUsername())));
	}

	@Test
	void shouldHaveTokenHandledByAuthenticationManager() {
		SignupRequest signUpRequest = HttpTestUtils.createSignupRequest(
				faker.internet().username(),
				faker.internet().emailAddress(),
				faker.internet().password(),
				RoleType.ROLE_MODERATOR);

		HttpTestUtils.signUp(signUpRequest, ApiEndpoints.AUTH_SIGN_UP_URL);

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				signUpRequest.getUsername(),
				signUpRequest.getPassword());

		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		assertTrue(authentication.isAuthenticated());
	}

	@Test
	void shouldAllowMultipleUsersInSecurityContext() {
		SignupRequest signUpRequestForUser1 = HttpTestUtils.createSignupRequest(
				faker.internet().username(),
				faker.internet().emailAddress(),
				faker.internet().password(),
				RoleType.ROLE_MODERATOR);

		SignupRequest signUpRequestForUser2 = HttpTestUtils.createSignupRequest(
				faker.internet().username(),
				faker.internet().emailAddress(),
				faker.internet().password(),
				RoleType.ROLE_MODERATOR);

		HttpTestUtils.signUp(signUpRequestForUser1, ApiEndpoints.AUTH_SIGN_UP_URL);
		HttpTestUtils.signUp(signUpRequestForUser2, ApiEndpoints.AUTH_SIGN_UP_URL);

		UserDetails user1 = customUserDetailsService.loadUserByUsername(signUpRequestForUser1.getUsername());
		UserDetails user2 = customUserDetailsService.loadUserByUsername(signUpRequestForUser2.getUsername());

		Authentication auth1 = new UsernamePasswordAuthenticationToken(user1, user1.getPassword(),
				user1.getAuthorities());

		AuthenticationUtils.setAuthentication(auth1);

		// Assert that SecurityContextHolder has user1
		Authentication currentAuth = AuthenticationUtils.getCurrentAuthentication();
		assertEquals("Expected user1 to be authenticated", user1.getUsername(), currentAuth.getName());

		// Second user logging in
		Authentication auth2 = new UsernamePasswordAuthenticationToken(user2, user2.getPassword(),
				user2.getAuthorities());

		AuthenticationUtils.setAuthentication(auth2);
		currentAuth = AuthenticationUtils.getCurrentAuthentication();

		assertEquals("Expected user2 to be authenticated", user2.getUsername(), currentAuth.getName());

		assertNotEquals("user1 should no longer be in Context", user1.getUsername(), currentAuth.getName());
	}
}
