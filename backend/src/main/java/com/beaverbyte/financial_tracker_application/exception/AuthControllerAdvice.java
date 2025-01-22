package com.beaverbyte.financial_tracker_application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class AuthControllerAdvice {

	@ExceptionHandler(value = UserAlreadyLoggedOutException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ProblemDetail handleUserAlreadyLoggedOutException(UserAlreadyLoggedOutException ex, WebRequest request) {
		return new ProblemDetail(
				"https://example.com/probs/unauthorized", // type
				"Unauthorized", // title
				HttpStatus.UNAUTHORIZED.value(), // status
				ex.getMessage(), // detail
				request.getDescription(false) // instance
		);
	}

	// @ExceptionHandler(value = UserAlreadyLoggedOutException.class)
	// @ResponseStatus(HttpStatus.UNAUTHORIZED)
	// public ErrorMessage
	// handleUserAlreadyLoggedOutException(UserAlreadyLoggedOutException ex,
	// WebRequest request) {
	// return new ErrorMessage(
	// HttpStatus.UNAUTHORIZED.value(),
	// new Date(),
	// ex.getMessage(),
	// request.getDescription(false));
	// }

}
