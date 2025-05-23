package com.beaverbyte.financial_tracker_application.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record TransactionRequest(
		Long id,
		LocalDate date,
		String account,
		String category,
		String merchant,
		@NotNull BigDecimal amount,
		String note) {
}