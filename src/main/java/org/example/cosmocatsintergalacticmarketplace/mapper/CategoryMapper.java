package org.example.cosmocatsintergalacticmarketplace.mapper;

import org.example.cosmocatsintergalacticmarketplace.domain.Category;
import org.example.cosmocatsintergalacticmarketplace.dto.CategoryDTO;
import org.example.cosmocatsintergalacticmarketplace.repositories.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toCategoryDTO(Category category);
    Category toCategoryDomain(CategoryDTO categoryDTO);

    Category toDomain(CategoryEntity entity);

    @Mapping(target = "description", ignore = true)
    CategoryEntity toEntity(Category category);
}