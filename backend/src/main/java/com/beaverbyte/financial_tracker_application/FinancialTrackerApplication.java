package com.beaverbyte.financial_tracker_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Wrapper that contains Spring App
 */
@SpringBootApplication
public class FinancialTrackerApplication {
	public static void main(String[] args) {
		SpringApplication.run(FinancialTrackerApplication.class, args);
	}
}
