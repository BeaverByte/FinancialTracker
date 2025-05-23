package com.beaverbyte.financial_tracker_application.exception;

public class TokenRefreshException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TokenRefreshException(String token, String message) {
		super(String.format("Failed for [%s]: %s", token, message));
	}
}