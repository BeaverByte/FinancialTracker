package com.beaverbyte.financial_tracker_application.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record TransactionAddRequest(
		LocalDate date,
		@NotNull Long accountId,
		@NotNull Long categoryId,
		@NotNull Long merchantId,
		@NotNull BigDecimal amount,
		String note) {
}