package com.beaverbyte.financial_tracker_application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.beaverbyte.financial_tracker_application.dto.response.AccountDTO;
import com.beaverbyte.financial_tracker_application.model.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {
	AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

	AccountDTO accountToDTO(Account account);

	default Account stringToAccount(String name) {
		Account account = new Account();
		account.setName(name);
		return account;
	}
}
