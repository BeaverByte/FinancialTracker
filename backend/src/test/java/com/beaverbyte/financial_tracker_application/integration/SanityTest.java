package com.beaverbyte.financial_tracker_application.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.metamodel.Metamodel;

public class SanityTest extends AbstractIntegrationTest {

	@Autowired
	private EntityManagerFactory emf;

	@Test
	void contextLoads() {
		// Verify app is able to load Spring context successfully
	}

	@Test
	void inspectEntities() {
		Metamodel metamodel = emf.getMetamodel();
		System.out.println("Entities:");
		metamodel.getEntities().forEach(e -> System.out.println(e.getName()));
	}

}
