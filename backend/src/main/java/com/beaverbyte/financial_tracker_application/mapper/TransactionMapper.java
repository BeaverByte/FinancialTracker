package com.beaverbyte.financial_tracker_application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.beaverbyte.financial_tracker_application.dto.request.TransactionRequest;
import com.beaverbyte.financial_tracker_application.model.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
	TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

	Transaction transactionDTOToTransaction(TransactionRequest transactionDTO);

	TransactionRequest transactionToTransactionDTO(Transaction transaction);
}
