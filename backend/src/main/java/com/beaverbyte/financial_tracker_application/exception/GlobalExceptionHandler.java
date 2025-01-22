package com.beaverbyte.financial_tracker_application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(UserLoginException.class)
	public ResponseEntity<String> userNotFoundExceptionHandler(UserLoginException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

	// Handle HTTP Method Request errors (e.g. if GET is used for a POST method)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	// @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public ResponseEntity<ErrorMessage> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
		ErrorMessage errors = new ErrorMessage(
				HttpStatus.METHOD_NOT_ALLOWED.value(),
				new Date(),
				ex.getMessage(),
				"Method not allowed for requested URL");

		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errors);
	}

	// Handle validation errors from @Valid annotation
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		// There can be multiple validation errors and messages
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return ResponseEntity.badRequest().body(errors);
	}
}