package com.beaverbyte.financial_tracker_application.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.beaverbyte.financial_tracker_application.dto.response.AccountDTO;
import com.beaverbyte.financial_tracker_application.dto.response.CategoryDTO;
import com.beaverbyte.financial_tracker_application.service.AccountService;
import com.beaverbyte.financial_tracker_application.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;

/**
 * Controller that handles requests (HTTP, etc.)
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api")
public class AccountRestController {

	private final AccountService accountService;

	public AccountRestController(AccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping("/accounts")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get all accounts")
	public List<AccountDTO> findAll() {
		return accountService.findAll();
	}
}
