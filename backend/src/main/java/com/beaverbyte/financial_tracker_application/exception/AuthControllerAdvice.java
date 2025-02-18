package com.beaverbyte.financial_tracker_application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class AuthControllerAdvice {

	@ExceptionHandler(value = RoleNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CustomProblemDetail handleRoleNotFoundException(RoleNotFoundException ex,
			WebRequest request) {
		return new CustomProblemDetail(
				HttpStatus.BAD_REQUEST.toString(),
				HttpStatus.BAD_REQUEST.value(),
				ex.getMessage(),
				request.getDescription(false));
	}

	@ExceptionHandler(value = SignupException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CustomProblemDetail handleSignupException(SignupException ex,
			WebRequest request) {
		return new CustomProblemDetail(
				HttpStatus.BAD_REQUEST.toString(),
				HttpStatus.BAD_REQUEST.value(),
				ex.getMessage(),
				request.getDescription(false));
	}

	@ExceptionHandler(value = UserNotLoggedInException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public CustomProblemDetail handleUserAlreadyLoggedOutException(UserNotLoggedInException ex,
			WebRequest request) {
		return new CustomProblemDetail(
				HttpStatus.UNAUTHORIZED.toString(),
				HttpStatus.UNAUTHORIZED.value(),
				ex.getMessage(),
				request.getDescription(false));
	}

}
