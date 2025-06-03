package com.beaverbyte.financial_tracker_application.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.beaverbyte.financial_tracker_application.dto.response.AccountDTO;
import com.beaverbyte.financial_tracker_application.mapper.AccountMapper;
import com.beaverbyte.financial_tracker_application.model.Account;
import com.beaverbyte.financial_tracker_application.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
	@Mock
	private AccountRepository accountRepository;
	@Mock
	private AccountMapper accountMapper;
	@InjectMocks
	private AccountService accountService;

	@Test
	void shouldShowListIfAccountsExist() {
		List<Account> accounts = new ArrayList<>(List.of(new Account(1L, "string")));
		List<AccountDTO> expectedDTOs = accounts.stream()
				.map(account -> new AccountDTO(account.getId(), account.getName())).toList();

		Mockito.when(accountRepository.findAll()).thenReturn(accounts);
		Mockito.when(accountMapper.accountToDTO(accounts.get(0))).thenReturn(expectedDTOs.get(0));

		List<AccountDTO> result = accountService.findAll();

		Assertions.assertFalse(result.isEmpty());
	}
}