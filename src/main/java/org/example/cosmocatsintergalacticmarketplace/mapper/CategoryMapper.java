package org.example.cosmocatsintergalacticmarketplace.mapper;

import org.example.cosmocatsintergalacticmarketplace.domain.Category;
import org.example.cosmocatsintergalacticmarketplace.dto.CategoryDTO;
import org.example.cosmocatsintergalacticmarketplace.repositories.entity.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toCategoryDTO(Category category);
    Category toCategoryDomain(CategoryDTO categoryDTO);

    Category toDomain(CategoryEntity entity);
    CategoryEntity toEntity(Category category);
}
