package com.beaverbyte.financial_tracker_application.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class OpenApiConfig {
	@Value("${spring.application.name}")
	private String applicationName;

	@Bean
	public OpenAPI baseOpenAPI() {
		Components components = new Components();

		return new OpenAPI().components(components)
				.info(new Info()
						.title(applicationName + " OpenAPI Docs")
						.version("1.0.0")
						.description("Documentation for endpoints, responses, and other API details"));
	}
}