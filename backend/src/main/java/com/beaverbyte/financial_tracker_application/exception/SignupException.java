package com.beaverbyte.financial_tracker_application.exception;

public class SignupException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SignupException(String message) {
		super(message);
	}
}
