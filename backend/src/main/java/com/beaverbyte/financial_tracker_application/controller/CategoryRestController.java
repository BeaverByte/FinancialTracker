package com.beaverbyte.financial_tracker_application.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.beaverbyte.financial_tracker_application.dto.response.CategoryDTO;
import com.beaverbyte.financial_tracker_application.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;

/**
 * Controller that handles requests (HTTP, etc.)
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CategoryRestController {

	private final CategoryService categoryService;

	public CategoryRestController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping("/categories")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get all categories")
	public List<CategoryDTO> findAll() {
		return categoryService.findAll();
	}

}
