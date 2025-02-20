package com.beaverbyte.financial_tracker_application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionDTO(Long id,
		LocalDate date,
		String merchant,
		String account,
		String category,
		BigDecimal amount,
		String note) {
}
