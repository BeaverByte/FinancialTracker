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
import com.beaverbyte.financial_tracker_application.exception.TransactionNotFoundException;
import com.beaverbyte.financial_tracker_application.mapper.TransactionMapper;
import com.beaverbyte.financial_tracker_application.model.Transaction;
import com.beaverbyte.financial_tracker_application.repository.TransactionRepository;

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
	private TransactionRequest transactionRequest;
	private Transaction mappedTransaction;

	@BeforeEach
	void setUp() {
		transactionRequest = createRandomTransactionRequest();
		mappedTransaction = TransactionMapper.INSTANCE.transactionDTOToTransaction(transactionRequest);
	}

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

	@Test
	void shouldAddTransaction() {
		Mockito.when(transactionRepository.save(mappedTransaction)).thenReturn(mappedTransaction);
		Mockito.when(transactionMapper.transactionDTOToTransaction(transactionRequest)).thenReturn(mappedTransaction);

		Transaction transaction = transactionService.add(transactionRequest);

		assertNotNull(transaction);
		assertEquals(transaction.getAccount(), transactionRequest.account());
	}

	@Test
	void shouldOverwriteIDWhenAddTransaction() {
		Mockito.when(transactionRepository.save(mappedTransaction)).thenReturn(mappedTransaction);
		Mockito.when(transactionMapper.transactionDTOToTransaction(transactionRequest)).thenReturn(mappedTransaction);

		Transaction transaction = transactionService.add(transactionRequest);

		assertNotEquals(transaction.getId(), transactionRequest.id().longValue());
	}

	@Test
	void shouldUpdateTransaction() {
		Mockito.when(transactionRepository.save(mappedTransaction)).thenReturn(mappedTransaction);
		Mockito.when(transactionMapper.transactionDTOToTransaction(transactionRequest)).thenReturn(mappedTransaction);

		Mockito.when(transactionRepository.findById(transactionRequest.id()))
				.thenReturn(Optional.of(mappedTransaction));

		Transaction transaction = transactionService.update(transactionRequest, faker.number().positive());

		assertNotNull(transaction);
	}

	@Test
	void shouldErrorOnNonExistentIDWhenUpdateTransaction() {
		assertThrows(TransactionNotFoundException.class,
				() -> transactionService.update(transactionRequest, -1));
	}

	@Test
	void shouldDeleteTransaction() {
		long randomId = faker.random().nextLong();
		Mockito.when(transactionRepository.findById(randomId)).thenReturn(Optional.of(mappedTransaction));
		transactionService.deleteById(randomId);
		verify(transactionRepository).deleteById(randomId);
	}

	@Test
	void shouldErrorOnNonExistentIDWhenDeleteTransaction() {
		assertThrows(TransactionNotFoundException.class,
				() -> transactionService.deleteById(-1));
	}

	@Test
	void shouldGetAllTransactions() {
		int page = 1;
		int size = 5;

		List<Transaction> transactions = List.of(mappedTransaction, mappedTransaction);

		Page<Transaction> transactionPage = new PageImpl<>(transactions);
		Pageable pageable = PageRequest.of(page - 1, size);

		Mockito.when(transactionRepository.findAll(pageable)).thenReturn(transactionPage);

		List<Transaction> result = transactionService.findAll(page, size);

		assertNotNull(result);
	}

	@Test
	void testFindById() {

	}

}
