package com.beaverbyte.financial_tracker_application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.dto.response.AccountDTO;
import com.beaverbyte.financial_tracker_application.dto.response.CategoryDTO;
import com.beaverbyte.financial_tracker_application.mapper.AccountMapper;
import com.beaverbyte.financial_tracker_application.mapper.CategoryMapper;
import com.beaverbyte.financial_tracker_application.repository.AccountRepository;
import com.beaverbyte.financial_tracker_application.repository.CategoryRepository;

@Service
public class AccountService {
	private final AccountRepository accountRepository;
	private final AccountMapper accountMapper;

	public AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
		this.accountRepository = accountRepository;
		this.accountMapper = accountMapper;
	}

	public List<AccountDTO> findAll() {
		return accountRepository.findAll()
				.stream()
				.map(accountMapper::accountToDTO)
				.toList();
	}
}
