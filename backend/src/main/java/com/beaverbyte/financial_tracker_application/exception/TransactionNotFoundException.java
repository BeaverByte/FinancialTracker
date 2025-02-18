package com.beaverbyte.financial_tracker_application.exception;

public class TransactionNotFoundException extends RuntimeException {

	public TransactionNotFoundException(String message) {
		super(message);
	}
}
