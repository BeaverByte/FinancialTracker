package com.beaverbyte.financial_tracker_application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.model.Transaction;
import com.beaverbyte.financial_tracker_application.repository.TransactionRepository;

/**
 * Logic and processing for Transactions
 */
@Service
public class TransactionService {

	private final TransactionRepository transactionRepository;

	public TransactionService(TransactionRepository transactionDAO) {
		this.transactionRepository = transactionDAO;
	}

	public List<Transaction> findAll() {
		return transactionRepository.findAll();
	}

	public Transaction findById(long id) {
		Optional<Transaction> searchedTransaction = transactionRepository.findById(id);

		Transaction transaction = null;

		if (searchedTransaction.isPresent()) {
			transaction = searchedTransaction.get();
		} else {
			throw new RuntimeException("Transaction ID not found - " + id);
		}

		return transaction;
	}

	public boolean existsById(long id) {
		Optional<Transaction> searchedTransaction = transactionRepository.findById(id);
		if (searchedTransaction.isPresent()) {
			return true;
		}
		return false;
	}

	public Transaction save(Transaction transaction) {
		return transactionRepository.save(transaction);
	}

	public void deleteById(long id) {
		transactionRepository.deleteById(id);
	}
}
