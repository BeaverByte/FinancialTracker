package com.beaverbyte.financial_tracker_application.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.dto.request.TransactionRequest;
import com.beaverbyte.financial_tracker_application.exception.TransactionNotFoundException;
import com.beaverbyte.financial_tracker_application.mapper.TransactionMapper;
import com.beaverbyte.financial_tracker_application.model.Transaction;
import com.beaverbyte.financial_tracker_application.repository.TransactionRepository;

/**
 * Logic and processing for Transactions
 */
@Service
public class TransactionService {

	private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

	private final TransactionRepository transactionRepository;
	private final TransactionMapper transactionMapper;

	public TransactionService(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
		this.transactionRepository = transactionRepository;
		this.transactionMapper = transactionMapper;
	}

	public List<Transaction> findAll(int page, int size) {
		if (size == 0) {
			return transactionRepository.findAll();
		}
		Pageable pageable = PageRequest.of(page - 1, size);
		Page<Transaction> transactionPage = transactionRepository.findAll(pageable);
		return transactionPage.stream().toList();
	}

	public Transaction findById(long id) {
		return transactionRepository.findById(id).orElseThrow(
				() -> new TransactionNotFoundException("Transaction does not exist with id " + id));
	}

	public Transaction add(TransactionRequest transactionRequest) {
		Transaction transaction = transactionMapper.transactionDTOToTransaction(transactionRequest);

		// Set id to 0 in case id is passed through JSON to force save of item instead
		// update
		transaction.setId(0);

		log.info("Transaction, {}, saved in database", transaction);
		return transactionRepository.save(transaction);
	}

	public Transaction update(TransactionRequest transactionRequest, int id) {
		if (!transactionRepository.findById(transactionRequest.id()).isPresent()) {
			throw new TransactionNotFoundException("Transaction does not exist with id " + transactionRequest.id());
		}

		Transaction transaction = transactionMapper.transactionDTOToTransaction(transactionRequest);

		// If id sent in body, set it to url ID to prevent incorrect update
		transaction.setId(id);

		log.info("Transaction, {}, updated in database", transaction);
		return transactionRepository.save(transaction);
	}

	public void deleteById(long id) {
		if (!transactionRepository.findById(id).isPresent()) {
			throw new TransactionNotFoundException("Transaction does not exist with id " + id);
		}

		log.info("Transaction, {}, deleted from database with id of ", id);
		transactionRepository.deleteById(id);
	}
}
