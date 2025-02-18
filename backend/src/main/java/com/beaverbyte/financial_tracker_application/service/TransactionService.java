package com.beaverbyte.financial_tracker_application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.exception.TransactionNotFoundException;
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

	public List<Transaction> findAll(int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, size);
		Page<Transaction> transactionPage = transactionRepository.findAll(pageable);
		return transactionPage.stream().toList();
	}

	public Transaction findById(long id) {
		return transactionRepository.findById(id).orElseThrow(
				() -> new TransactionNotFoundException("Transaction does not exist with id " + id));
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
