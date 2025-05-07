package com.beaverbyte.financial_tracker_application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.beaverbyte.financial_tracker_application.dto.response.CategoryDTO;
import com.beaverbyte.financial_tracker_application.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
	CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

	CategoryDTO categoryToDTO(Category category);

}
