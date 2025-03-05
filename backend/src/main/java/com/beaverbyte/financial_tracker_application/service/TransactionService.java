package com.beaverbyte.financial_tracker_application.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.dto.request.TransactionRequest;
import com.beaverbyte.financial_tracker_application.dto.response.TransactionDTO;
import com.beaverbyte.financial_tracker_application.exception.TransactionNotFoundException;
import com.beaverbyte.financial_tracker_application.mapper.TransactionMapper;
import com.beaverbyte.financial_tracker_application.model.Transaction;
import com.beaverbyte.financial_tracker_application.repository.TransactionRepository;
import com.beaverbyte.financial_tracker_application.util.PropertyValidator;

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

	public List<TransactionDTO> findAll(int page, int size) {
		List<Transaction> transactions;

		if (size == 0) {
			transactions = transactionRepository.findAll();
			return transactions.stream()
					.map(transactionMapper::transactionToTransactionDTO)
					.toList();
		}

		Pageable pageable = PageRequest.of(page - 1, size);
		Page<Transaction> transactionPage = transactionRepository.findAll(pageable);
		transactions = transactionPage.stream().toList();

		return transactions.stream()
				.map(transactionMapper::transactionToTransactionDTO)
				.toList();
	}

	public Page<TransactionDTO> findByFilter(Pageable pageable) {
		log.info("Initial pageable is {}", pageable);
		log.info("Pageable sorts are {}", pageable.getSort());

		validatePageable(pageable.getSort());

		Page<Transaction> requestedTransactions = transactionRepository.findAll(pageable);

		Page<TransactionDTO> transactions = requestedTransactions.map(transactionMapper::transactionToTransactionDTO);

		log.info("Page of TransactionsDTO are {}", transactions);

		return transactions;
	}

	public void validatePageable(Sort sort) {
		if (sort.isUnsorted())
			return;

		List<Order> properties = sort.toList();

		log.info("Validating sort properties, {}", properties);

		List<String> misMatchedProperties = PropertyValidator.getMismatchedProperties(properties, Transaction.class);

		if (!misMatchedProperties.isEmpty()) {
			throw new TransactionNotFoundException("Invalid sort field(s): " + misMatchedProperties);
		}
	}

	public TransactionDTO findById(long id) {
		Transaction transaction = transactionRepository.findById(id).orElseThrow(
				() -> new TransactionNotFoundException("Transaction does not exist with id " + id));
		return transactionMapper.transactionToTransactionDTO(transaction);
	}

	public TransactionDTO add(TransactionRequest transactionRequest) {
		Transaction transaction = transactionMapper.transactionRequestToTransaction(transactionRequest);

		// Set id to 0 in case id is passed through JSON to force save of item instead
		// update
		log.info("Transaction id, {}, setting to 0", transaction.getId());
		transaction.setId(0);

		log.info("Transaction, {}, saved in database", transaction);
		Transaction savedTransaction = transactionRepository.save(transaction);

		return transactionMapper.transactionToTransactionDTO(savedTransaction);
	}

	public TransactionDTO update(TransactionRequest transactionRequest, long id) {
		log.info("update() called with ID: {}", id);

		if (!transactionRepository.findById(id).isPresent()) {
			throw new TransactionNotFoundException("Transaction does not exist with id " + id);
		}

		Transaction transaction = transactionMapper.transactionRequestToTransaction(transactionRequest);

		// If id sent in body, set it to url ID to prevent incorrect update

		log.info("Transaction id, {}, setting to {}", transaction.getId(), id);
		transaction.setId(id);

		log.info("Transaction, {}, updated in database", transaction);
		Transaction savedTransaction = transactionRepository.save(transaction);

		return transactionMapper.transactionToTransactionDTO(savedTransaction);
	}

	public void deleteById(long id) {
		if (!transactionRepository.findById(id).isPresent()) {
			throw new TransactionNotFoundException("Transaction does not exist with id " + id);
		}

		log.info("Transaction, {}, deleted from database with id of ", id);
		transactionRepository.deleteById(id);
	}
}
