package org.example.cosmocatsintergalacticmarketplace.service;

import org.example.cosmocatsintergalacticmarketplace.domain.Category;
import org.example.cosmocatsintergalacticmarketplace.mapper.CategoryMapper;
import org.example.cosmocatsintergalacticmarketplace.repositories.CategoryRepository;
import org.example.cosmocatsintergalacticmarketplace.repositories.entity.CategoryEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl service;

    @Test
    void shouldCreateNewCategory() {
        // GIVEN
        Category inputCategory = new Category(null, "Space Gear");
        CategoryEntity entityToSave = new CategoryEntity(null, "Space Gear", null);
        CategoryEntity savedEntity = new CategoryEntity(1L, "Space Gear", null);
        Category resultDomain = new Category(1L, "Space Gear");

        when(categoryMapper.toEntity(inputCategory)).thenReturn(entityToSave);
        when(categoryRepository.save(entityToSave)).thenReturn(savedEntity);
        when(categoryMapper.toDomain(savedEntity)).thenReturn(resultDomain);

        // WHEN
        Category created = service.create(inputCategory);

        // THEN
        assertNotNull(created.getId());
        assertEquals("Space Gear", created.getName());
        verify(categoryRepository).save(entityToSave);
    }

    @Test
    void shouldFindAllCategories() {
        // GIVEN
        CategoryEntity entity = new CategoryEntity(1L, "Cat A", "Desc");
        Category domain = new Category(1L, "Cat A");

        when(categoryRepository.findAll()).thenReturn(List.of(entity));
        when(categoryMapper.toDomain(entity)).thenReturn(domain);

        // WHEN
        List<Category> categories = service.findAll();

        // THEN
        assertFalse(categories.isEmpty());
        assertEquals(1, categories.size());
        assertEquals("Cat A", categories.get(0).getName());
    }

    @Test
    void shouldReturnEmptyListWhenNoCategoriesExist() {
        // GIVEN
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        // WHEN
        List<Category> categories = service.findAll();

        // THEN
        assertTrue(categories.isEmpty());
    }

    @Test
    void shouldFindCategoryById() {
        // GIVEN
        Long id = 1L;
        CategoryEntity entity = new CategoryEntity(id, "Weapons", "Desc");
        Category domain = new Category(id, "Weapons");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(entity));
        when(categoryMapper.toDomain(entity)).thenReturn(domain);

        // WHEN
        Optional<Category> found = service.findById(id);

        // THEN
        assertTrue(found.isPresent());
        assertEquals("Weapons", found.get().getName());
    }

    @Test
    void shouldReturnEmptyIfNotFound() {
        // GIVEN
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN
        Optional<Category> found = service.findById(99L);

        // THEN
        assertTrue(found.isEmpty());
    }

    @Test
    void shouldUpdateExistingCategory() {
        // GIVEN
        Long id = 1L;
        Category updateInfo = new Category(null, "New Name");

        CategoryEntity existingEntity = new CategoryEntity(id, "Old Name", "Desc");
        CategoryEntity updatedEntity = new CategoryEntity(id, "New Name", "Desc"); // Те, що поверне save
        Category domainResult = new Category(id, "New Name");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(categoryRepository.save(existingEntity)).thenReturn(updatedEntity);
        when(categoryMapper.toDomain(updatedEntity)).thenReturn(domainResult);

        // WHEN
        Optional<Category> result = service.update(id, updateInfo);

        // THEN
        assertTrue(result.isPresent());
        assertEquals("New Name", result.get().getName());
        verify(categoryRepository).save(existingEntity);
    }

    @Test
    void shouldReturnEmptyWhenUpdatingNonExisting() {
        // GIVEN
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());
        Category fake = new Category(null, "Fake");

        // WHEN
        Optional<Category> result = service.update(999L, fake);

        // THEN
        assertTrue(result.isEmpty());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void shouldDeleteExistingCategory() {
        // GIVEN
        Long id = 1L;
        when(categoryRepository.existsById(id)).thenReturn(true);

        // WHEN
        boolean deleted = service.delete(id);

        // THEN
        assertTrue(deleted);
        verify(categoryRepository).deleteById(id);
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExisting() {
        // GIVEN
        Long id = 999L;
        when(categoryRepository.existsById(id)).thenReturn(false);

        // WHEN
        boolean deleted = service.delete(id);

        // THEN
        assertFalse(deleted);
        verify(categoryRepository, never()).deleteById(any());
    }
}