package org.example.cosmocatsintergalacticmarketplace.service;

import lombok.RequiredArgsConstructor;
import org.example.cosmocatsintergalacticmarketplace.domain.Category;
import org.example.cosmocatsintergalacticmarketplace.mapper.CategoryMapper;
import org.example.cosmocatsintergalacticmarketplace.repositories.CategoryRepository;
import org.example.cosmocatsintergalacticmarketplace.repositories.entity.CategoryEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public Category create(Category category) {
        CategoryEntity entity = categoryMapper.toEntity(category);
        CategoryEntity savedEntity = categoryRepository.save(entity);
        return categoryMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDomain);
    }

    @Override
    @Transactional
    public Optional<Category> update(Long id, Category category) {
        return categoryRepository.findById(id)
                .map(existingEntity -> {
                    existingEntity.setName(category.getName());
                    return categoryRepository.save(existingEntity);
                })
                .map(categoryMapper::toDomain);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}