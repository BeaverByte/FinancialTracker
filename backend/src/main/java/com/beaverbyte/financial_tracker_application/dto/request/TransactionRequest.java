package com.beaverbyte.financial_tracker_application.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(Long id,
		LocalDate date,
		String merchant,
		String account,
		String category,
		BigDecimal amount,
		String note) {
}
