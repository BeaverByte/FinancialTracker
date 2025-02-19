package com.beaverbyte.financial_tracker_application.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.beaverbyte.financial_tracker_application.dto.request.TransactionRequest;
import com.beaverbyte.financial_tracker_application.mapper.TransactionMapper;
import com.beaverbyte.financial_tracker_application.model.Transaction;

class TransactionMapperTest {

	LocalDate testDate = LocalDate.of(2020, 1, 5);

	@Test
	void shouldMapTransactionRequestToTransaction() {
		Transaction transaction = new Transaction();
		transaction.setDate(testDate);
		transaction.setAccount(null);
		transaction.setCategory(null);
		transaction.setMerchant(null);
		transaction.setNote(null);

		TransactionRequest transactionDTO = TransactionMapper.INSTANCE.transactionToTransactionDTO(transaction);

		assertNotNull(transactionDTO);
		assertEquals(transactionDTO.account(), transaction.getAccount());
	}

	@Test
	void shouldMapTransactionToTransactionRequest() {
		TransactionRequest transactionDTO = new TransactionRequest(1L,
				testDate,
				"Mcronalds",
				"Bank",
				"Food",
				new BigDecimal(99.00),
				"Justanote");

		Transaction transaction = TransactionMapper.INSTANCE.transactionDTOToTransaction(transactionDTO);

		assertNotNull(transactionDTO);
		assertEquals(transactionDTO.account(), transaction.getAccount());
	}
}
