package com.beaverbyte.financial_tracker_application.exception;

public class RoleNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RoleNotFoundException(String message) {
		super(message);
	}
}
