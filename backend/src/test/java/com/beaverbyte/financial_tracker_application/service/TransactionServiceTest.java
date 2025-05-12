package com.beaverbyte.financial_tracker_application.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.List;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.beaverbyte.financial_tracker_application.dto.request.TransactionRequest;
import com.beaverbyte.financial_tracker_application.dto.response.TransactionDTO;
import com.beaverbyte.financial_tracker_application.exception.EntityNotFoundException;
import com.beaverbyte.financial_tracker_application.mapper.TransactionMapper;
import com.beaverbyte.financial_tracker_application.model.Transaction;
import com.beaverbyte.financial_tracker_application.repository.TransactionRepository;
import com.beaverbyte.financial_tracker_application.utils.JpaTestUtils;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
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
	void shouldAddTransaction() {
		TransactionRequest transactionRequest = new TransactionRequest(null, null, null, null, null, null, null);

		Transaction transaction = new Transaction(0, null, null, null, null, null, null);

		TransactionDTO transactionDTO = new TransactionDTO(0L, null, null, null, null, null, null);

		Mockito.when(transactionMapper.transactionRequestToTransaction(transactionRequest))
				.thenReturn(transaction);

		Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction);

		Mockito.when(transactionMapper.transactionToTransactionDTO(transaction)).thenReturn(transactionDTO);

		TransactionDTO result = transactionService.add(transactionRequest);

		assertNotNull(result);
		assertEquals(result.account(), transactionRequest.account());
	}

	@Test
	void shouldOverwriteIDWhenAddTransaction() {
		TransactionRequest transactionRequest = new TransactionRequest(null, null, null, null, null, null, null);
		Transaction transaction = new Transaction(0, null, null, null, null, null, null);

		Mockito.when(transactionMapper.transactionRequestToTransaction(transactionRequest))
				.thenReturn(transaction);

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
	void shouldGetAllTransactions() {
		Transaction transaction1 = new Transaction(1, null, null, null, null, null, null);
		Transaction transaction2 = new Transaction(2, null, null, null, null, null, null);

		int requestedPage = 1;
		int requestedSize = 5;

		List<Transaction> transactions = List.of(transaction1, transaction2);

		Page<Transaction> transactionPage = new PageImpl<>(transactions);
		Pageable pageable = PageRequest.of(requestedPage - 1, requestedSize);

		Mockito.when(transactionRepository.findAll(pageable)).thenReturn(transactionPage);

		List<TransactionDTO> result = transactionService.findAll(requestedPage, requestedSize);

		assertNotNull(result);
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
