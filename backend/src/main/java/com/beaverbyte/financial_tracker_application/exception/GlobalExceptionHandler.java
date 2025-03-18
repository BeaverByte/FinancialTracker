package com.beaverbyte.financial_tracker_application.exception;

import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserLoginException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CustomProblemDetail userNotFoundExceptionHandler(UserLoginException exception, WebRequest request) {
		return new CustomProblemDetail(
				HttpStatus.BAD_REQUEST.toString(),
				HttpStatus.BAD_REQUEST.value(),
				exception.getMessage(),
				request.getDescription(false));
	}

	@ExceptionHandler(PropertyReferenceException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CustomProblemDetail badPropertyReferenceExceptionHandler(PropertyReferenceException exception,
			WebRequest request) {
		return new CustomProblemDetail(
				HttpStatus.BAD_REQUEST.toString(),
				HttpStatus.BAD_REQUEST.value(),
				exception.getMessage(),
				request.getDescription(false));
	}

	// Handle HTTP Request with null RequestBody
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CustomProblemDetail handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
			WebRequest request) {

		return new CustomProblemDetail(
				HttpStatus.BAD_REQUEST.toString(),
				HttpStatus.BAD_REQUEST.value(),
				exception.getMessage(),
				request.getDescription(false));
	}

	// Handle HTTP Method Request errors (e.g. if GET is used for a POST method)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public CustomProblemDetail handleMethodNotAllowed(HttpRequestMethodNotSupportedException exception,
			WebRequest request) {
		return new CustomProblemDetail(
				HttpStatus.BAD_REQUEST.toString(),
				HttpStatus.BAD_REQUEST.value(),
				exception.getMessage(),
				request.getDescription(false));
	}

	// Handle validation errors from @Valid annotation
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CustomProblemDetail handleValidationExceptions(MethodArgumentNotValidException exception,
			WebRequest request) {
		Map<String, String> errors = new HashMap<>();
		// There can be multiple validation errors and messages
		exception.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		return new CustomProblemDetail(
				HttpStatus.BAD_REQUEST.toString(),
				HttpStatus.BAD_REQUEST.value(),
				"Validation(s) failed",
				request.getDescription(false),
				errors);
	}

	@ExceptionHandler(TransactionNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CustomProblemDetail handleTransactionNotFoundException(TransactionNotFoundException exception,
			WebRequest request) {
		return new CustomProblemDetail(
				HttpStatus.BAD_REQUEST.toString(),
				HttpStatus.BAD_REQUEST.value(),
				exception.getMessage(),
				request.getDescription(false));
	}

	@ExceptionHandler(value = TokenRefreshException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public CustomProblemDetail handleTokenRefreshException(TokenRefreshException exception, WebRequest request) {
		return new CustomProblemDetail(
				HttpStatus.FORBIDDEN.toString(),
				HttpStatus.FORBIDDEN.value(),
				exception.getMessage(),
				request.getDescription(false));
	}
}