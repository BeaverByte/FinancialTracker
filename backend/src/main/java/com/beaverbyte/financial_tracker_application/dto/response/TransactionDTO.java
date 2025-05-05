package com.beaverbyte.financial_tracker_application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.beaverbyte.financial_tracker_application.model.Account;
import com.beaverbyte.financial_tracker_application.model.Category;
import com.beaverbyte.financial_tracker_application.model.Merchant;

public record TransactionDTO(Long id,
		LocalDate date,
		Merchant merchant,
		Account account,
		Category category,
		BigDecimal amount,
		String note) {
}
