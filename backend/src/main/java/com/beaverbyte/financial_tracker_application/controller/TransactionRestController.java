package com.beaverbyte.financial_tracker_application.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.beaverbyte.financial_tracker_application.dto.request.TransactionRequest;
import com.beaverbyte.financial_tracker_application.dto.response.TransactionDTO;
import com.beaverbyte.financial_tracker_application.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.PutMapping;

/**
 * Controller that handles requests (HTTP, etc.)
 */

// @CrossOrigin(origins = "*", maxAge = 3600)
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api")
public class TransactionRestController {

	private final TransactionService transactionService;

	public TransactionRestController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@GetMapping("/transactions")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get all/paginated transactions")
	public List<TransactionDTO> findAll(
			@Parameter(description = "Number of pages to contain entities") @RequestParam(defaultValue = "1") @Min(1) int page,
			@Parameter(description = "Amount of entities allowed on a given page. Value of 0 will provide all entities") @RequestParam(defaultValue = "0") @Min(0) int size) {
		return transactionService.findAll(page, size);
	}

	@GetMapping("/transactions/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get a single transaction")
	public TransactionDTO getTransaction(@PathVariable @Min(1) int id) {
		return transactionService.findById(id);
	}

	@PostMapping("/transactions")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create a transaction")
	public TransactionDTO addTransaction(
			@Valid @RequestBody TransactionRequest transactionRequest) {
		return transactionService.add(transactionRequest);
	}

	@PutMapping("/transactions/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Update transaction")
	public TransactionDTO updateTransaction(@PathVariable @Min(1) int id,
			@RequestBody TransactionRequest transactionRequest) {
		return transactionService.update(transactionRequest, id);
	}

	@DeleteMapping("/transactions/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTransaction(@PathVariable long id) {
		transactionService.deleteById(id);
	}
}
