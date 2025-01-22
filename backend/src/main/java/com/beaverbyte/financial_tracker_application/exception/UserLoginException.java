package com.beaverbyte.financial_tracker_application.exception;

public class UserLoginException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserLoginException(String message) {
		super(message);
	}
}
