package com.beaverbyte.financial_tracker_application.exception;

public class UserAlreadyLoggedOutException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserAlreadyLoggedOutException(String message) {
		super(message);
	}
}
