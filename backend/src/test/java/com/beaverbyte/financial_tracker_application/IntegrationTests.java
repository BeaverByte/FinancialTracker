package com.beaverbyte.financial_tracker_application;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.beaverbyte.financial_tracker_application.dto.api.request.SignupRequest;
import com.beaverbyte.financial_tracker_application.entity.ERole;
import com.beaverbyte.financial_tracker_application.entity.Role;
import com.beaverbyte.financial_tracker_application.entity.User;
import com.beaverbyte.financial_tracker_application.mapper.SignupRequestMapper;
import com.beaverbyte.financial_tracker_application.repository.RefreshTokenRepository;
import com.beaverbyte.financial_tracker_application.repository.RoleRepository;
import com.beaverbyte.financial_tracker_application.repository.UserRepository;
import com.beaverbyte.financial_tracker_application.service.RoleService;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
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

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;

        sanitizeRepos();
        seedTestContainers();

        System.out.println("Database cleared before each test");
    }

    void sanitizeRepos() {
        // Sanitizing repos

        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        refreshTokenRepository.flush();
        userRepository.flush();
        roleRepository.flush();
    }

    void seedTestContainers() {
        // Seeding TestContainers with roles
        Role roleUser = new Role(ERole.ROLE_USER);
        roleRepository.save(roleUser);
        Role roleMod = new Role(ERole.ROLE_MODERATOR);
        roleRepository.save(roleMod);
        Role roleAdmin = new Role(ERole.ROLE_ADMIN);
        roleRepository.save(roleAdmin);
    }

    Response signUp(SignupRequest signupRequest) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(SignupRequestMapper.toMap(signupRequest))
                .when()
                .post("/api/auth/signup")
                .then()
                .extract().response();
    }

    Response signIn(String username, String password) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(Map.of(
                        "username", username,
                        "password", password))
                .when()
                .post("/api/auth/signin")
                .then()
                .extract().response();
    }

    String authenticateUserAndGetToken(String username, String password) throws Exception {
        Response response = signIn(username, password);

        String accessToken = response.jsonPath().getString("accessToken");

        // Check if token is found
        if (accessToken == null || accessToken.isEmpty()) {
            throw new RuntimeException("Access token not found in the response.");
        }

        return accessToken;
    }

    Response sendGETHTTPJwtRequest(String path, String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(path)
                .then()
                .extract().response();
    }

    @Test
    void shouldAllowAuthorizedUserAccessToProtectedRoute() throws Exception {
        SignupRequest signUpRequest = createSignupRequest("dumblikebricks",
                "dumbemail@gmail.com",
                "dumbpassword",
                ERole.ROLE_MODERATOR);

        signUpUser(signUpRequest);

        Response signInResponse = signIn(signUpRequest.getUsername(), signUpRequest.getPassword());

        String token = authenticateUserAndGetToken(signUpRequest.getUsername(), signUpRequest.getPassword());
        String path = "api/test/user";
        Response response = sendGETHTTPJwtRequest(path, token);

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
        SignupRequest signUpRequest = createSignupRequest("dumblikebricks",
                "dumbemail@gmail.com",
                "dumbpassword",
                ERole.ROLE_MODERATOR);

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
            ERole.ROLE_MODERATOR);

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
        assertTrue(roles.stream().anyMatch(role -> role.getName().equals(ERole.ROLE_USER)));
        assertTrue(roles.stream().anyMatch(role -> role.getName().equals(ERole.ROLE_MODERATOR)));
        assertTrue(roles.stream().anyMatch(role -> role.getName().equals(ERole.ROLE_ADMIN)));
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

    Response signUpUser(SignupRequest signUpRequest) {
        return signUp(signUpRequest);
    }

    SignupRequest createSignupRequest(String username, String email, String password, ERole role) {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setUsername(username);
        signUpRequest.setEmail(email);
        signUpRequest.setPassword(password);
        signUpRequest.setRole(getRolesForInput(role));

        return signUpRequest;
    }

    Set<String> getRolesForInput(ERole role) {
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
            ERole.ROLE_MODERATOR);

        Response response = signUpUser(signUpRequest);

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
        ERole.ROLE_MODERATOR);

        signUpUser(signUpRequest);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signUpRequest.getUsername(),
                signUpRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        assertTrue(authentication.isAuthenticated());
    }
}
