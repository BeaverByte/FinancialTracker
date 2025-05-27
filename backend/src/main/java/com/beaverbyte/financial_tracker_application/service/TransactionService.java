package com.beaverbyte.financial_tracker_application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.dto.request.TransactionRequest;
import com.beaverbyte.financial_tracker_application.dto.response.TransactionDTO;
import com.beaverbyte.financial_tracker_application.exception.EntityNotFoundException;
import com.beaverbyte.financial_tracker_application.mapper.TransactionMapper;
import com.beaverbyte.financial_tracker_application.model.Account;
import com.beaverbyte.financial_tracker_application.model.Category;
import com.beaverbyte.financial_tracker_application.model.Merchant;
import com.beaverbyte.financial_tracker_application.model.Transaction;
import com.beaverbyte.financial_tracker_application.repository.AccountRepository;
import com.beaverbyte.financial_tracker_application.repository.CategoryRepository;
import com.beaverbyte.financial_tracker_application.repository.MerchantRepository;
import com.beaverbyte.financial_tracker_application.repository.TransactionRepository;

/**
 * Logic and processing for Transactions
 */
@Service
public class TransactionService {

	private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

	private final TransactionRepository transactionRepository;
	private final MerchantRepository merchantRepository;
	private final AccountRepository accountRepository;
	private final CategoryRepository categoryRepository;
	private final TransactionMapper transactionMapper;

	public TransactionService(TransactionRepository transactionRepository, MerchantRepository merchantRepository,
			AccountRepository accountRepository, CategoryRepository categoryRepository,
			TransactionMapper transactionMapper) {
		this.transactionRepository = transactionRepository;
		this.merchantRepository = merchantRepository;
		this.accountRepository = accountRepository;
		this.categoryRepository = categoryRepository;
		this.transactionMapper = transactionMapper;
	}

	public Page<TransactionDTO> findByFilter(Pageable pageable) {
		log.info("Initial pageable is {}", pageable);
		log.info("Pageable sorts are {}", pageable.getSort());

		Page<Transaction> requestedTransactions = transactionRepository.findAll(pageable);
		Page<TransactionDTO> transactions = requestedTransactions
				.map(transactionMapper::transactionToTransactionDTO);

		log.info("Page of TransactionsDTO are {}", transactions);

		return transactions;
	}

	public TransactionDTO findById(long id) {
		Transaction transaction = transactionRepository.findById(id).orElseThrow(
				() -> new EntityNotFoundException("Transaction does not exist with id " + id));
		return transactionMapper.transactionToTransactionDTO(transaction);
	}

	public TransactionDTO add(TransactionRequest transactionRequest) {

		Transaction transaction = transactionMapper.transactionRequestToTransaction(transactionRequest);

		if (transactionRequest.category() != null) {
			Category category = categoryRepository.findByName(transactionRequest.category())
					.orElseThrow(() -> new EntityNotFoundException(
							"Category not found: " + transactionRequest.category()));
			transaction.setCategory(category);
		}

		if (transactionRequest.merchant() != null) {
			Merchant merchant = merchantRepository.findByName(transactionRequest.merchant())
					.orElseThrow(
							() -> new EntityNotFoundException(
									"Merchant not found: " + transactionRequest.merchant()));
			transaction.setMerchant(merchant);
		}

		if (transactionRequest.account() != null) {
			Account account = accountRepository.findByName(transactionRequest.account())
					.orElseThrow(() -> new EntityNotFoundException(
							"Account not found: " + transactionRequest.account()));
			transaction.setAccount(account);
		}

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

		Transaction transaction = transactionRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Transaction does not exist with id " + id));

		transactionMapper.transactionRequestToTransaction(transactionRequest, transaction);

		if (transactionRequest.category() != null) {
			Category category = categoryRepository.findByName(transactionRequest.category())
					.orElseThrow(() -> new EntityNotFoundException(
							"Category not found: " + transactionRequest.category()));
			transaction.setCategory(category);
		}

		if (transactionRequest.merchant() != null) {
			Merchant merchant = merchantRepository.findByName(transactionRequest.merchant())
					.orElseThrow(
							() -> new EntityNotFoundException(
									"Merchant not found: " + transactionRequest.merchant()));
			transaction.setMerchant(merchant);
		}

		if (transactionRequest.account() != null) {
			Account account = accountRepository.findByName(transactionRequest.account())
					.orElseThrow(() -> new EntityNotFoundException(
							"Account not found: " + transactionRequest.account()));
			transaction.setAccount(account);
		}

		// If id sent in body, set it to url ID to prevent incorrect update
		log.info("Transaction id, {}, setting to {}", transaction.getId(), id);
		transaction.setId(id);

		log.info("Transaction, {}, updated in database", transaction);
		Transaction savedTransaction = transactionRepository.save(transaction);

		return transactionMapper.transactionToTransactionDTO(savedTransaction);
	}

	public void deleteById(long id) {
		if (!transactionRepository.findById(id).isPresent()) {
			throw new EntityNotFoundException("Transaction does not exist with id " + id);
		}

		log.info("Transaction, {}, deleted from database with id of ", id);
		transactionRepository.deleteById(id);
	}
}