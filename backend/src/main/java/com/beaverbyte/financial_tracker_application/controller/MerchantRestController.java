package com.beaverbyte.financial_tracker_application.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.beaverbyte.financial_tracker_application.dto.response.MerchantDTO;
import com.beaverbyte.financial_tracker_application.service.MerchantService;

import io.swagger.v3.oas.annotations.Operation;

/**
 * Controller that handles requests (HTTP, etc.)
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api")
public class MerchantRestController {

	private final MerchantService merchantService;

	public MerchantRestController(MerchantService merchantService) {
		this.merchantService = merchantService;
	}

	@GetMapping("/merchants")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get all merchants")
	public List<MerchantDTO> findAll() {
		return merchantService.findAll();
	}

	// @PostMapping("/categories")
	// @ResponseStatus(HttpStatus.CREATED)
	// @Operation(summary = "Create a transaction")
	// public TransactionDTO addTransaction(
	// @Valid @RequestBody TransactionRequest transactionRequest) {
	// return categoryService.add(transactionRequest);
	// }

	// // @PutMapping("/categories/{id}")
	// // @ResponseStatus(HttpStatus.OK)
	// // @Operation(summary = "Update transaction")
	// // public TransactionDTO updateTransaction(@PathVariable @Min(1) long id,
	// // @RequestBody TransactionRequest transactionRequest) {
	// // return categoryService.update(transactionRequest, id);
	// // }
}
