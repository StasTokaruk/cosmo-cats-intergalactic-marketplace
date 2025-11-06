package org.example.cosmocatsintergalacticmarketplace.service;

import org.example.cosmocatsintergalacticmarketplace.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category create(Category category);
    List<Category> findAll();
    Optional<Category> findById(Long id);
    Optional<Category> update(Long id, Category category);
    boolean delete(Long id);
}
