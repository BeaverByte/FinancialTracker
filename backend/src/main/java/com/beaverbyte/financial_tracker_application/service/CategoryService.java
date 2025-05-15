package com.beaverbyte.financial_tracker_application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.dto.response.CategoryDTO;
import com.beaverbyte.financial_tracker_application.mapper.CategoryMapper;
import com.beaverbyte.financial_tracker_application.repository.CategoryRepository;

@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;

	public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
		this.categoryRepository = categoryRepository;
		this.categoryMapper = categoryMapper;
	}

	public List<CategoryDTO> findAll() {
		return categoryRepository.findAll()
				.stream()
				.map(categoryMapper::categoryToDTO)
				.toList();
	}
}
