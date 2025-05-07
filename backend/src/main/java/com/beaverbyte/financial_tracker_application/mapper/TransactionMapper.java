package com.beaverbyte.financial_tracker_application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.beaverbyte.financial_tracker_application.dto.request.TransactionAddRequest;
import com.beaverbyte.financial_tracker_application.dto.request.TransactionRequest;
import com.beaverbyte.financial_tracker_application.dto.response.TransactionDTO;
import com.beaverbyte.financial_tracker_application.model.Transaction;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper {
	TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

	Transaction transactionRequestToTransaction(TransactionRequest transactionRequest,
			@MappingTarget Transaction transaction);

	Transaction transactionAddRequestToTransaction(TransactionAddRequest transactionAddRequest);

	@Mapping(source = "merchant.name", target = "merchant")
	@Mapping(source = "account.name", target = "account")
	@Mapping(source = "category.name", target = "category")
	TransactionDTO transactionToTransactionDTO(Transaction transaction);
}
