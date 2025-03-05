package com.beaverbyte.financial_tracker_application.util;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.data.domain.Sort.Order;

public class PropertyValidator {

	public static List<String> getMismatchedProperties(List<Order> properties, Class<?> clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("Class cannot be null");
		}

		Field[] fields = clazz.getDeclaredFields();

		List<String> fieldNames = properties.stream()
				.map(Order::getProperty)
				.toList();

		List<String> classFieldNames = List.of(fields).stream()
				.map(Field::getName)
				.toList();

		return fieldNames.stream()
				.filter(name -> !classFieldNames.contains(name))
				.toList();
	}
}