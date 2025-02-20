package com.beaverbyte.financial_tracker_application.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.beaverbyte.financial_tracker_application.dto.request.TransactionRequest;
import com.beaverbyte.financial_tracker_application.dto.response.TransactionDTO;
import com.beaverbyte.financial_tracker_application.model.Transaction;

class TransactionMapperTest {

	LocalDate testDate = LocalDate.of(2020, 1, 5);

	@Test
	void shouldMapTransactionToTransactionDTO() {
		Transaction transaction = new Transaction();
		transaction.setDate(testDate);
		transaction.setAccount(null);
		transaction.setCategory(null);
		transaction.setMerchant(null);
		transaction.setNote(null);

		TransactionDTO transactionDTO = TransactionMapper.INSTANCE.transactionToTransactionDTO(transaction);

		assertNotNull(transactionDTO);
		assertEquals(transactionDTO.account(), transaction.getAccount());
	}

	@Test
	void shouldMapTransactionRequestToTransaction() {
		TransactionRequest transactionRequest = new TransactionRequest(1L,
				testDate,
				"Mcronalds",
				"Bank",
				"Food",
				new BigDecimal(99.00),
				"Justanote");

		Transaction transaction = TransactionMapper.INSTANCE.transactionRequestToTransaction(transactionRequest);

		assertNotNull(transactionRequest);
		assertEquals(transactionRequest.account(), transaction.getAccount());
	}
}
