package com.beaverbyte.financial_tracker_application.integration;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.beaverbyte.financial_tracker_application.constants.ApiEndpoints;
import com.beaverbyte.financial_tracker_application.dto.request.SignupRequest;
import com.beaverbyte.financial_tracker_application.dto.request.TransactionRequest;
import com.beaverbyte.financial_tracker_application.dto.response.TransactionDTO;
import com.beaverbyte.financial_tracker_application.model.RoleType;
import com.beaverbyte.financial_tracker_application.model.Transaction;
import com.beaverbyte.financial_tracker_application.model.Account;
import com.beaverbyte.financial_tracker_application.model.Category;
import com.beaverbyte.financial_tracker_application.model.Merchant;
import com.beaverbyte.financial_tracker_application.model.Role;
import com.beaverbyte.financial_tracker_application.repository.AccountRepository;
import com.beaverbyte.financial_tracker_application.repository.CategoryRepository;
import com.beaverbyte.financial_tracker_application.repository.MerchantRepository;
import com.beaverbyte.financial_tracker_application.repository.RefreshTokenRepository;
import com.beaverbyte.financial_tracker_application.repository.RoleRepository;
import com.beaverbyte.financial_tracker_application.repository.TransactionRepository;
import com.beaverbyte.financial_tracker_application.repository.UserRepository;
import com.beaverbyte.financial_tracker_application.security.CustomUserDetailsService;
import com.beaverbyte.financial_tracker_application.service.RefreshTokenService;
import com.beaverbyte.financial_tracker_application.service.RoleService;
import com.beaverbyte.financial_tracker_application.service.TransactionService;
import com.beaverbyte.financial_tracker_application.utils.HttpTestUtils;
import com.beaverbyte.financial_tracker_application.utils.JpaTestUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.datafaker.Faker;

class TransactionIntegrationTests extends AbstractIntegrationTest {

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
	TransactionRepository transactionRepository;

	@Autowired
	RefreshTokenRepository refreshTokenRepository;

	@Autowired
	RoleService roleService;

	@Autowired
	TransactionService transactionService;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@Autowired
	RefreshTokenService refreshTokenService;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	MerchantRepository merchantRepository;

	@Autowired
	AccountRepository accountRepository;

	@Value("${JWT_COOKIE_NAME}")
	private String jwtCookieName;

	@Value("${JWT_REFRESH_COOKIE_NAME}")
	private String jwtRefreshCookieName;

	private String sessionCookie;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost:" + port;

		sanitizeRepos();
		seedTestContainers();
		SecurityContextHolder.clearContext();

		System.out.println("Database cleared before each test");

		SignupRequest signUpRequest = HttpTestUtils.createSignupRequest(
				faker.internet().username(),
				faker.internet().emailAddress(),
				faker.internet().password(),
				RoleType.ROLE_ADMIN);

		HttpTestUtils.signUp(signUpRequest, ApiEndpoints.AUTH_SIGN_UP_URL);

		sessionCookie = HttpTestUtils.signInAndGetSessionCookie(signUpRequest.getUsername(),
				signUpRequest.getPassword(),
				ApiEndpoints.AUTH_SIGN_IN_URL,
				jwtCookieName);
	}

	private void sanitizeRepos() {
		// Sanitizing repos

		JpaTestUtils.clearRepository(refreshTokenRepository);
		JpaTestUtils.clearRepository(userRepository);
		JpaTestUtils.clearRepository(roleRepository);
		JpaTestUtils.clearRepository(transactionRepository);

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
	void shouldReturnAllTransactionsIfFindAllRequestSizeIsZero() {
		List<Transaction> transactions = List.of(new Transaction(), new Transaction(), new Transaction());
		transactionRepository.saveAll(transactions);

		Response response = HttpTestUtils.sendGETRequestWithHeaders(
				ApiEndpoints.TRANSACTIONS_URL + "?size=0",
				Map.of("Cookie", sessionCookie));

		JsonPath jsonPath = response.jsonPath();
		int transactionsCount = jsonPath.getList("content").size();

		int expectedCount = 3;

		assertEquals(expectedCount, transactionsCount);
	}

	@Test
	void shouldReturnLimitedTransactionsIfFindAllRequestSizeIsLessThanTransactions() {
		List<Transaction> transactions = List.of(new Transaction(), new Transaction(), new Transaction());
		transactionRepository.saveAll(transactions);

		Response response = HttpTestUtils.sendGETRequestWithHeaders(
				ApiEndpoints.TRANSACTIONS_URL + "?size=1",
				Map.of("Cookie", sessionCookie));

		JsonPath jsonPath = response.jsonPath();
		int transactionsCount = jsonPath.getList("content").size();

		int expectedCount = 1;

		assertEquals(expectedCount, transactionsCount);
	}

	@Test
	void shouldSetCategoryIfProvidedWhenTransactionAdded() {
		String categoryName = "yup";
		Category category = new Category(1L, categoryName);
		categoryRepository.save(category);

		String merchantName = "merchant";
		Merchant merchant = new Merchant(1L, merchantName);
		merchantRepository.save(merchant);

		String accountName = "account";
		Account account = new Account(1L, accountName);
		accountRepository.save(account);

		TransactionRequest transactionRequest = new TransactionRequest(null,
				null,
				accountName,
				categoryName,
				merchantName,
				new BigDecimal(9001),
				null);

		Response response = given()
				.header("Cookie", sessionCookie)
				.contentType(ContentType.JSON)
				.body(transactionRequest)
				.when()
				.post(ApiEndpoints.TRANSACTIONS_URL)
				.then()
				.extract()
				.response();

		TransactionDTO transactionDTO = response.as(TransactionDTO.class);

		// need to adjust Service to account for nullish nested Entities
		assertEquals(categoryName, transactionDTO.category());
	}
}
