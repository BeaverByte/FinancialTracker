package com.beaverbyte.financial_tracker_application.service;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.beaverbyte.financial_tracker_application.dto.request.TransactionRequest;
import com.beaverbyte.financial_tracker_application.model.Transaction;
import com.beaverbyte.financial_tracker_application.repository.TransactionRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
	@Mock
	private TransactionRepository transactionRepository;

	@InjectMocks
	private TransactionService transactionService;

	@Test
	void shouldAddTransaction() {
		Faker faker = new Faker();

		Transaction transaction = new Transaction();
		TransactionRequest transactionRequest = new TransactionRequest(faker.number().randomNumber(),
				faker.timeAndDate().birthday(),
				faker.eldenRing().npc(),
				faker.restaurant().name(),
				faker.random().toString(),
				new BigDecimal(faker.number().randomNumber()),
				faker.witcher().quote());

		Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(transaction);

		Transaction result = transactionService.add(transactionRequest);

		Assertions.assertNotNull(result);
		Mockito.verify(transactionRepository, Mockito.times(1)).save(Mockito.any(Transaction.class));
	}

	@Test
	void testDeleteById() {

	}

	@Test
	void testExistsById() {

	}

	@Test
	void testFindAll() {

	}

	@Test
	void testFindById() {

	}

	@Test
	void testUpdate() {

	}
}
