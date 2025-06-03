package com.beaverbyte.financial_tracker_application.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.beaverbyte.financial_tracker_application.dto.request.TransactionRequest;
import com.beaverbyte.financial_tracker_application.dto.response.TransactionDTO;
import com.beaverbyte.financial_tracker_application.model.Account;
import com.beaverbyte.financial_tracker_application.model.Category;
import com.beaverbyte.financial_tracker_application.model.Merchant;
import com.beaverbyte.financial_tracker_application.model.Transaction;

@ExtendWith(MockitoExtension.class)
class TransactionMapperTest {

	@InjectMocks
	private TransactionMapperImpl transactionMapper;

	@Mock
	private MerchantMapper merchantMapper;

	@Mock
	private CategoryMapper categoryMapper;

	@Mock
	private AccountMapper accountMapper;

	LocalDate testDate = LocalDate.of(2020, 1, 5);

	@Test
	void shouldMapTransactionToTransactionDTO() {
		Transaction transaction = new Transaction();
		transaction.setDate(testDate);
		transaction.setAccount(new Account(1L, "yum"));
		transaction.setCategory(new Category(1L, "yum"));
		transaction.setMerchant(new Merchant(1L, "yum"));
		transaction.setNote(null);
		transaction.setAmount(new BigDecimal(30000000L));

		TransactionDTO transactionDTO = transactionMapper.transactionToTransactionDTO(transaction);

		assertNotNull(transactionDTO);
		assertEquals(transactionDTO.amount(), transaction.getAmount());
	}

	@Test
	void shouldMapTransactionRequestToTransaction() {
		TransactionRequest transactionRequest = new TransactionRequest(1L,
				testDate,
				"Bank",
				"Food",
				"Mcronalds",
				new BigDecimal(99.00),
				"Justanote");

		when(merchantMapper.stringToMerchant("Mcronalds"))
				.thenReturn(new Merchant(1L, "Mcronalds"));
		when(accountMapper.stringToAccount("Bank"))
				.thenReturn(new Account(1L, "Bank"));
		when(categoryMapper.stringToCategory("Food"))
				.thenReturn(new Category(1L, "Food"));

		Transaction transaction = transactionMapper.transactionRequestToTransaction(transactionRequest);

		assertNotNull(transactionRequest);
		assertEquals(transactionRequest.amount(), transaction.getAmount());
	}
}