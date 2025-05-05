package com.beaverbyte.financial_tracker_application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.beaverbyte.financial_tracker_application.dto.request.TransactionRequest;
import com.beaverbyte.financial_tracker_application.dto.response.TransactionDTO;
import com.beaverbyte.financial_tracker_application.model.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
	TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

	@Mapping(target = "merchant", ignore = true)
	@Mapping(target = "account", ignore = true)
	@Mapping(target = "category", ignore = true)
	Transaction transactionRequestToTransaction(TransactionRequest transactionRequest);

	TransactionDTO transactionToTransactionDTO(Transaction transaction);
}
