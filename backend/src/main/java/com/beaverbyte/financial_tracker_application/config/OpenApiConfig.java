package com.beaverbyte.financial_tracker_application.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;

@OpenAPIDefinition
@Configuration
public class OpenApiConfig {

	// @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	// @Bean
	// public OpenAPI openAPI() {
	// return new OpenAPI().addSecurityItem(new
	// SecurityRequirement().addList("Bearer Authentication"))
	// .components(new Components().addSecuritySchemes("Bearer Authentication",
	// createAPIKeyScheme()))
	// .info(new Info().title("My REST API")
	// .description("Some custom description of API.")
	// .version("1.0").contact(new Contact().name("Sallo Szrajbman")
	// .email("www.baeldung.com").url("salloszraj@gmail.com"))
	// .license(new License().name("License of API")
	// .url("API license URL")));
	// }

	@Bean
	public OpenAPI baseOpenAPI() {
		Components components = new Components();

		return new OpenAPI().components(components)
				.info(new Info()
						.title("Springboot_Swagger Project OpenAPI Docs")
						.version("1.0.0").description("Doc Description"));
	}

	// private SecurityScheme createAPIKeyScheme() {
	// return new SecurityScheme().type(SecurityScheme.Type.HTTP)
	// .bearerFormat("JWT")
	// .scheme("bearer");
	// }

	// private final String moduleName;
	// private final String apiVersion;

	// public OpenApiConfig(
	// @Value("${module-name}") String moduleName,
	// @Value("${api-version}") String apiVersion) {
	// this.moduleName = moduleName;
	// this.apiVersion = apiVersion;
	// }

	// @Bean
	// public OpenAPI customOpenAPI() {
	// final String securitySchemeName = "bearerAuth";
	// final String apiTitle = String.format("%s API", moduleName);
	// return new OpenAPI()
	// .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
	// .components(
	// new Components()
	// .addSecuritySchemes(securitySchemeName,
	// new SecurityScheme()
	// .name(securitySchemeName)
	// .type(SecurityScheme.Type.HTTP)
	// .scheme("bearer")
	// .bearerFormat("JWT")))
	// .info(new Info().title(apiTitle).version(apiVersion));
	// }

}