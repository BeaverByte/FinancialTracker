package com.beaverbyte.financial_tracker_application.exception;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CustomProblemDetail {
	private String title;
	private int status;
	private String detail;
	private String instance;

	@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude when null
	private Map<String, String> validationErrors;

	// Default constructor
	public CustomProblemDetail() {
	}

	public CustomProblemDetail(String title, int status, String detail, String instance,
			Map<String, String> validationErrors) {
		this.title = title;
		this.status = status;
		this.detail = detail;
		this.instance = instance;
		this.validationErrors = validationErrors;
	}

	public CustomProblemDetail(String title, int status, String detail, String instance) {
		this.title = title;
		this.status = status;
		this.detail = detail;
		this.instance = instance;
	}

	// Static factory method
	public static CustomProblemDetail forStatusAndDetail(Integer status, String detail) {
		CustomProblemDetail problemDetail = new CustomProblemDetail();
		problemDetail.setStatus(status != null ? status : 500); // Default to 500 if null
		problemDetail.setDetail(detail != null ? detail : "An unexpected error occurred.");
		return problemDetail;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public String getTitle() {
		return title;
	}

	public int getStatus() {
		return status;
	}

	public String getDetail() {
		return detail;
	}

	public String getInstance() {
		return instance;
	}

	public Map<String, String> getValidationErrors() {
		return validationErrors;
	}

	public void setValidationErrors(Map<String, String> validationErrors) {
		this.validationErrors = validationErrors;
	}
}