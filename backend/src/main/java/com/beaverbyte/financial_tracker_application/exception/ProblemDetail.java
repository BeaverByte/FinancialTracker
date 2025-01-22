package com.beaverbyte.financial_tracker_application.exception;

public class ProblemDetail {
	private String type;
	private String title;
	private int status;
	private String detail;
	private String instance;

	// Default constructor
	public ProblemDetail() {
	}

	// All-args constructor
	public ProblemDetail(String type, String title, int status, String detail, String instance) {
		this.type = type;
		this.title = title;
		this.status = status;
		this.detail = detail;
		this.instance = instance;
	}

	// Static factory method
	public static ProblemDetail forStatusAndDetail(Integer status, String detail) {
		ProblemDetail problemDetail = new ProblemDetail();
		problemDetail.setStatus(status != null ? status : 500); // Default to 500 if null
		problemDetail.setDetail(detail != null ? detail : "An unexpected error occurred.");
		return problemDetail;
	}

	// Getters and setters
	public void setType(String type) {
		this.type = type;
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

	public String getType() {
		return type;
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
}