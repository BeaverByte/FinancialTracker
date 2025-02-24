package com.beaverbyte.financial_tracker_application.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
		TransactionRequest transactionRequest = createRandomTransactionRequest();
		Transaction transaction = TransactionMapper.INSTANCE.transactionRequestToTransaction(transactionRequest);

		Mockito.when(transactionMapper.transactionRequestToTransaction(transactionRequest))
				.thenReturn(transaction);

		Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction);

		TransactionDTO transactionDTO = TransactionMapper.INSTANCE.transactionToTransactionDTO(transaction);

		Mockito.when(transactionMapper.transactionToTransactionDTO(transaction)).thenReturn(transactionDTO);

		TransactionDTO result = transactionService.add(transactionRequest);

		assertNotNull(result);
		assertEquals(result.account(), transactionRequest.account());
	}

	@Test
	void shouldOverwriteIDWhenAddTransaction() {
		TransactionRequest transactionRequest = createRandomTransactionRequest();
		Transaction transaction = TransactionMapper.INSTANCE.transactionRequestToTransaction(transactionRequest);

		Mockito.when(transactionMapper.transactionRequestToTransaction(transactionRequest))
				.thenReturn(transaction);

		Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction);

		transactionService.add(transactionRequest);

		assertEquals(0, transaction.getId());
	}

	@Test
	void shouldUpdateTransaction() {
		TransactionRequest transactionRequest = createRandomTransactionRequest();
		Transaction mappedTransaction = TransactionMapper.INSTANCE.transactionRequestToTransaction(transactionRequest);
		TransactionDTO transactionDTO = TransactionMapper.INSTANCE.transactionToTransactionDTO(mappedTransaction);

		Mockito.when(transactionRepository.save(mappedTransaction)).thenReturn(mappedTransaction);
		Mockito.when(transactionMapper.transactionRequestToTransaction(transactionRequest))
				.thenReturn(mappedTransaction);

		Mockito.when(transactionRepository.findById(transactionRequest.id()))
				.thenReturn(Optional.of(mappedTransaction));

		Mockito.when(transactionMapper.transactionToTransactionDTO(Mockito.any(Transaction.class)))
				.thenReturn(transactionDTO);

		TransactionDTO result = transactionService.update(transactionRequest, faker.number().positive());

		assertNotNull(result);
	}

	@Test
	void shouldUpdateTransactionIDToParamIDWhenUpdateTransaction() {
		TransactionRequest transactionRequest = createRandomTransactionRequest();
		Transaction transaction = TransactionMapper.INSTANCE.transactionRequestToTransaction(transactionRequest);
		TransactionDTO transactionDTO = TransactionMapper.INSTANCE.transactionToTransactionDTO(transaction);

		long initialTransactionID = transaction.getId();

		Mockito.when(transactionMapper.transactionRequestToTransaction(transactionRequest))
				.thenReturn(transaction);

		Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction);

		Mockito.when(transactionRepository.findById(transactionRequest.id()))
				.thenReturn(Optional.of(transaction));

		Mockito.when(transactionMapper.transactionToTransactionDTO(Mockito.any(Transaction.class)))
				.thenReturn(transactionDTO);

		int updateParam = faker.number().positive();

		transactionService.update(transactionRequest, updateParam);

		assertEquals(transaction.getId(), updateParam);
		assertNotEquals(transaction.getId(), initialTransactionID);
	}

	@Test
	void shouldErrorOnNonExistentIDWhenUpdateTransaction() {
		TransactionRequest transactionRequest = createRandomTransactionRequest();

		assertThrows(TransactionNotFoundException.class,
				() -> transactionService.update(transactionRequest, -1));
	}

	@Test
	void shouldDeleteTransaction() {
		TransactionRequest transactionRequest = createRandomTransactionRequest();
		Transaction mappedTransaction = TransactionMapper.INSTANCE.transactionRequestToTransaction(transactionRequest);

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
		TransactionRequest transactionRequest = createRandomTransactionRequest();
		Transaction mappedTransaction = TransactionMapper.INSTANCE.transactionRequestToTransaction(transactionRequest);

		int page = 1;
		int size = 5;

		List<Transaction> transactions = List.of(mappedTransaction, mappedTransaction);

		Page<Transaction> transactionPage = new PageImpl<>(transactions);
		Pageable pageable = PageRequest.of(page - 1, size);

		Mockito.when(transactionRepository.findAll(pageable)).thenReturn(transactionPage);

		List<TransactionDTO> result = transactionService.findAll(page, size);

		assertNotNull(result);
	}

	@Test
	void shouldFindById() {
		TransactionRequest transactionRequest = createRandomTransactionRequest();
		Transaction mappedTransaction = TransactionMapper.INSTANCE.transactionRequestToTransaction(transactionRequest);
		TransactionDTO transactionDTO = TransactionMapper.INSTANCE.transactionToTransactionDTO(mappedTransaction);

		long id = faker.number().randomNumber();

		Mockito.when(transactionRepository.findById(id)).thenReturn(Optional.of(mappedTransaction));
		Mockito.when(transactionMapper.transactionToTransactionDTO(mappedTransaction)).thenReturn(transactionDTO);

		TransactionDTO transaction = transactionService.findById(id);

		assertNotNull(transaction);
	}

}
