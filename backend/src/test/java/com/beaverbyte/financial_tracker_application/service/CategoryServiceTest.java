package com.beaverbyte.financial_tracker_application.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.beaverbyte.financial_tracker_application.dto.response.CategoryDTO;
import com.beaverbyte.financial_tracker_application.mapper.CategoryMapper;
import com.beaverbyte.financial_tracker_application.model.Category;
import com.beaverbyte.financial_tracker_application.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
	@Mock
	private CategoryRepository categoryRepository;
	@Mock
	private CategoryMapper categoryMapper;
	@InjectMocks
	private CategoryService categoryService;

	@Test
	void shouldShowListIfCategoriesExist() {
		List<Category> categories = new ArrayList<>(List.of(new Category(1L, "string")));
		List<CategoryDTO> expectedDTOs = categories.stream()
				.map(category -> new CategoryDTO(category.getId(), category.getName())).toList();

		Mockito.when(categoryRepository.findAll()).thenReturn(categories);
		Mockito.when(categoryMapper.categoryToDTO(categories.get(0))).thenReturn(expectedDTOs.get(0));

		List<CategoryDTO> result = categoryService.findAll();

		Assertions.assertFalse(result.isEmpty());
	}
}