package com.beaverbyte.financial_tracker_application;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

// Booting up Spring app context for end to end behavior, HTTP Requests, etc.
@SpringBootTest(
    classes = FinancialTrackerApplication.class,
    webEnvironment = WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
abstract class AbstractIntegrationTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        "postgres:16-alpine"
    );
    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    // static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:6-alpine"))
    //     .withExposedPorts(6379);

    // @DynamicPropertySource
    // static void redisProperties(DynamicPropertyRegistry registry) {
    //     redis.start();
    //     registry.add("spring.redis.host", redis::getHost);
    //     registry.add("spring.redis.port", redis::getFirstMappedPort);
    // }
}
