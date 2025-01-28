package com.beaverbyte.financial_tracker_application.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beaverbyte.financial_tracker_application.model.Transaction;
import com.beaverbyte.financial_tracker_application.service.TransactionService;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * Controller that handles requests (HTTP, etc.)
 */

@RestController
@RequestMapping("/api")
public class TransactionRestController {

	private TransactionService transactionService;

	public TransactionRestController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@GetMapping("/transactions")
	public ResponseEntity<List<Transaction>> findAll() {
		return ResponseEntity.status(HttpStatus.OK).body(transactionService.findAll());
	}

	@GetMapping("/transactions/{id}")
	public ResponseEntity<Transaction> getTransaction(@PathVariable int id) {
		if (!transactionService.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		Transaction transaction = transactionService.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(transaction);
	}

	@PostMapping("/transactions")
	public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction) {
		try {
			// Set id to 0 in case id is passed through JSON to force save of item instead
			// update
			transaction.setId(0);
			Transaction savedTransaction = transactionService.save(transaction);
			return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/transactions/{id}")
	public ResponseEntity<Transaction> updateTransaction(@PathVariable int id, @RequestBody Transaction transaction) {
		try {
			if (!transactionService.existsById(id)) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			// If id sent in body, set it to url ID to prevent incorrect update
			transaction.setId(id);
			return new ResponseEntity<>(transactionService.save(transaction), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/transactions/{id}")
	public ResponseEntity<HttpStatus> deleteTransaction(@PathVariable long id) {
		try {
			if (!transactionService.existsById(id)) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			transactionService.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
