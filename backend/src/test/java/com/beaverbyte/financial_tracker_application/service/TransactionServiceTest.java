package com.beaverbyte.financial_tracker_application.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.beaverbyte.financial_tracker_application.dto.request.TransactionRequest;
import com.beaverbyte.financial_tracker_application.dto.response.TransactionDTO;
import com.beaverbyte.financial_tracker_application.exception.EntityNotFoundException;
import com.beaverbyte.financial_tracker_application.mapper.TransactionMapper;
import com.beaverbyte.financial_tracker_application.model.Account;
import com.beaverbyte.financial_tracker_application.model.Category;
import com.beaverbyte.financial_tracker_application.model.Merchant;
import com.beaverbyte.financial_tracker_application.model.Transaction;
import com.beaverbyte.financial_tracker_application.repository.AccountRepository;
import com.beaverbyte.financial_tracker_application.repository.CategoryRepository;
import com.beaverbyte.financial_tracker_application.repository.MerchantRepository;
import com.beaverbyte.financial_tracker_application.repository.TransactionRepository;
import com.beaverbyte.financial_tracker_application.utils.JpaTestUtils;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private MerchantRepository merchantRepository;

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private TransactionMapper transactionMapper;

	@InjectMocks
	private TransactionService transactionService;

	@Captor
	ArgumentCaptor<Transaction> argumentCaptor;

	private Faker faker = new Faker();

	private TransactionRequest createRandomTransactionRequest() {
		return new TransactionRequest(
				faker.number().randomNumber(),
				faker.timeAndDate().birthday(),
				faker.eldenRing().npc(),
				faker.restaurant().name(),
				faker.random().toString(),
				new BigDecimal(faker.number().randomNumber()),
				faker.witcher().quote());
	}

	@BeforeEach
	void setUp() {

		sanitizeRepos();

		System.out.println("Database cleared before each test");
	}

	private void sanitizeRepos() {
		// Sanitizing repos

		JpaTestUtils.clearRepository(transactionRepository);
	}

	@Test
	void shouldErrorIfMissingFieldWhenAddTransaction() {
		// Arrange
		Category category = new Category(1L, "Test Category");
		// Account account = new Account(1L, "Test Account");
		Merchant merchant = new Merchant(1L, "Test Merchant");

		TransactionRequest transactionRequest = new TransactionRequest(
				null,
				LocalDate.of(2025, 12, 15),
				null,
				category.getName(),
				merchant.getName(),
				new BigDecimal(100.00),
				"Note");

		Transaction transaction = new Transaction(0, null, null, null, null, null, null);

		Mockito.when(transactionMapper.transactionRequestToTransaction(transactionRequest)).thenReturn(transaction);

		assertThrows(EntityNotFoundException.class, () -> {
			transactionService.add(transactionRequest);
		});
	}

	@Test
	void shouldAddTransactionWhenFieldsCorrect() {
		// Arrange
		Category category = new Category(1L, "Test Category");
		Account account = new Account(1L, "Test Account");
		Merchant merchant = new Merchant(1L, "Test Merchant");

		TransactionRequest transactionRequest = new TransactionRequest(
				null,
				LocalDate.of(2025, 12, 15),
				account.getName(),
				category.getName(),
				merchant.getName(),
				new BigDecimal(100.00),
				"Note");

		Transaction transaction = new Transaction();

		TransactionDTO expectedTransactionDTO = new TransactionDTO(1L,
				LocalDate.of(2025, 12, 15),
				merchant.getName(),
				account.getName(),
				category.getName(),
				new BigDecimal(100.00),
				"Note");

		Mockito.when(transactionMapper.transactionRequestToTransaction(transactionRequest)).thenReturn(transaction);
		Mockito.when(categoryRepository.findByName(Mockito.any())).thenReturn(Optional.of(category));
		Mockito.when(accountRepository.findByName(Mockito.any())).thenReturn(Optional.of(account));
		Mockito.when(merchantRepository.findByName(Mockito.any())).thenReturn(Optional.of(merchant));
		Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction);
		Mockito.when(transactionMapper.transactionToTransactionDTO(transaction)).thenReturn(expectedTransactionDTO);

		// Act
		TransactionDTO result = transactionService.add(transactionRequest);

		assertNotNull(result);
		assertEquals(expectedTransactionDTO, result);
	}

	@Test
	void shouldErrorOnNullishCategoryWhenAddTransaction() {
		TransactionRequest request = new TransactionRequest(
				null,
				null,
				"Test Account",
				null,
				"Test Merchant",
				new BigDecimal("100.00"),
				"Note");

		Transaction transaction = new Transaction();
		Mockito.when(transactionMapper.transactionRequestToTransaction(request)).thenReturn(transaction);

		assertThrows(EntityNotFoundException.class, () -> {
			transactionService.add(request);
		});
	}

	@Test
	void shouldErrorOnNullishAccountWhenAddTransaction() {
		TransactionRequest request = new TransactionRequest(
				null,
				null,
				null,
				"Test Category",
				"Test Merchant",
				new BigDecimal("100.00"),
				"Note");

		Transaction transaction = new Transaction();
		Mockito.when(transactionMapper.transactionRequestToTransaction(request)).thenReturn(transaction);

		assertThrows(EntityNotFoundException.class, () -> {
			transactionService.add(request);
		});
	}

	@Test
	void shouldOverwriteIDWhenAddTransaction() {
		// Arrange
		Category category = new Category(1L, "Test Category");
		Account account = new Account(1L, "Test Account");
		Merchant merchant = new Merchant(1L, "Test Merchant");

		TransactionRequest transactionRequest = new TransactionRequest(null,
				null,
				account.getName(),
				category.getName(),
				merchant.getName(),
				null,
				null);
		Long transactionID = 7L;
		Transaction transaction = new Transaction(transactionID, null, null, null, null, null, null);

		Mockito.when(transactionMapper.transactionRequestToTransaction(transactionRequest)).thenReturn(transaction);
		Mockito.when(categoryRepository.findByName(Mockito.any())).thenReturn(Optional.of(category));
		Mockito.when(accountRepository.findByName(Mockito.any())).thenReturn(Optional.of(account));
		Mockito.when(merchantRepository.findByName(Mockito.any())).thenReturn(Optional.of(merchant));
		// Transaction ID should be set to 0 after validation
		Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction);

		transactionService.add(transactionRequest);

		assertEquals(0, transaction.getId());
	}

	@Test
	void shouldUpdateTransaction() {
		TransactionRequest transactionRequest = new TransactionRequest(null, null, null, null, null, null, null);
		Transaction transaction = new Transaction(0, null, null, null, null, null, null);

		long requestedTransactionID = faker.number().randomNumber();

		TransactionDTO transactionDTO = new TransactionDTO(requestedTransactionID, null, null, null, null, null, null);

		Mockito.when(transactionRepository.findById(requestedTransactionID))
				.thenReturn(Optional.of(transaction));
		Mockito.when(transactionMapper.transactionRequestToTransaction(Mockito.any(TransactionRequest.class),
				Mockito.any(Transaction.class)))
				.thenReturn(transaction);
		Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction);
		Mockito.when(transactionMapper.transactionToTransactionDTO(Mockito.any(Transaction.class)))
				.thenReturn(transactionDTO);

		TransactionDTO result = transactionService.update(transactionRequest, requestedTransactionID);

		assertNotNull(result);
		assertEquals(transactionDTO, result);
	}

	@Test
	void shouldUpdateTransactionIDToParamIDWhenUpdateTransaction() {
		TransactionRequest transactionRequest = new TransactionRequest(null, null, null, null, null, null, null);
		Transaction transaction = new Transaction(0, null, null, null, null, null, null);

		long initialTransactionID = transaction.getId();
		long requestedTransactionID = faker.number().randomNumber();

		TransactionDTO transactionDTO = new TransactionDTO(requestedTransactionID, null, null, null, null, null, null);

		Mockito.when(transactionRepository.findById(requestedTransactionID))
				.thenReturn(Optional.of(transaction));
		Mockito.when(transactionMapper.transactionRequestToTransaction(Mockito.any(TransactionRequest.class),
				Mockito.any(Transaction.class)))
				.thenReturn(transaction);
		Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction);
		Mockito.when(transactionMapper.transactionToTransactionDTO(Mockito.any(Transaction.class)))
				.thenReturn(transactionDTO);

		transactionService.update(transactionRequest, requestedTransactionID);

		assertEquals(transaction.getId(), requestedTransactionID);
		assertNotEquals(transaction.getId(), initialTransactionID);
	}

	@Test
	void shouldErrorOnNonExistentIDWhenUpdateTransaction() {
		TransactionRequest transactionRequest = createRandomTransactionRequest();

		assertThrows(EntityNotFoundException.class,
				() -> transactionService.update(transactionRequest, -1));
	}

	@Test
	void shouldDeleteTransaction() {
		Transaction transaction = new Transaction(0, null, null, null, null, null, null);

		long requestId = faker.random().nextLong();
		Mockito.when(transactionRepository.findById(requestId)).thenReturn(Optional.of(transaction));
		transactionService.deleteById(requestId);
		verify(transactionRepository).deleteById(requestId);
	}

	@Test
	void shouldErrorOnNonExistentIDWhenDeleteTransaction() {
		assertThrows(EntityNotFoundException.class,
				() -> transactionService.deleteById(-1));
	}

	@Test
	void shouldFindById() {
		long id = faker.number().randomNumber();

		Transaction transaction = new Transaction(0, null, null, null, null, null, null);
		transaction.setId(id);

		TransactionDTO expectedDTO = new TransactionDTO(id, null, null, null, null, null, null);

		Mockito.when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));
		Mockito.when(transactionMapper.transactionToTransactionDTO(transaction)).thenReturn(expectedDTO);

		TransactionDTO result = transactionService.findById(id);

		assertNotNull(result);
	}

	@Test
	void shouldErrorOnNonExistentIDWhenFindById() {
		Long id = faker.number().randomNumber();

		assertThrows(EntityNotFoundException.class,
				() -> transactionService.findById(id));
	}

}
