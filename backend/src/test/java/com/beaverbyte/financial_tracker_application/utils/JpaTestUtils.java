package com.beaverbyte.financial_tracker_application.utils;

import org.springframework.data.jpa.repository.JpaRepository;

public class JpaTestUtils {
	public static <T> void clearRepository(JpaRepository<T, ?> repository) {
		repository.deleteAll();
		repository.flush();
	}
}
