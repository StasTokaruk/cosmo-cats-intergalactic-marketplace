package org.example.cosmocatsintergalacticmarketplace.service;

import org.example.cosmocatsintergalacticmarketplace.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceImplTest {

    private CategoryServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CategoryServiceImpl();
    }

    @Test
    void shouldCreateNewCategoryAndAssignId() {
        Category category = new Category(null, "Space Gear");

        Category created = service.create(category);

        assertNotNull(created.getId(), "Створена категорія має отримати ID");
        assertEquals("Space Gear", created.getName());
        assertEquals(1, service.findAll().size(), "У сховищі має бути 1 категорія");
    }

    @Test
    void shouldGenerateSequentialIds() {
        Category first = service.create(new Category(null, "Category A"));
        Category second = service.create(new Category(null, "Category B"));
        Category third = service.create(new Category(null, "Category C"));

        assertEquals(1L, first.getId());
        assertEquals(2L, second.getId());
        assertEquals(3L, third.getId());
        assertTrue(second.getId() > first.getId(), "Наступний ID має бути більшим за попередній");
    }

    @Test
    void shouldReturnEmptyListWhenNoCategoriesExist() {
        List<Category> categories = service.findAll();
        assertTrue(categories.isEmpty());
    }

    @Test
    void shouldFindCategoryByIdAfterCreation() {
        Category created = service.create(new Category(null, "Weapons"));

        Optional<Category> found = service.findById(created.getId());

        assertTrue(found.isPresent());
        assertEquals("Weapons", found.get().getName());
    }

    @Test
    void shouldReturnEmptyIfNotFound() {
        Optional<Category> found = service.findById(99L);
        assertTrue(found.isEmpty());
    }

    @Test
    void shouldUpdateExistingCategory() {
        Category existing = service.create(new Category(null, "Old Name"));
        Long existingId = existing.getId();

        Category update = new Category(null, "New Name");

        Optional<Category> result = service.update(existingId, update);

        assertTrue(result.isPresent());
        assertEquals(existingId, result.get().getId(), "ID оновленої категорії має збігатися");
        assertEquals("New Name", result.get().getName(), "Назва має бути оновлена");
        assertEquals("New Name", service.findById(existingId).get().getName());
    }

    @Test
    void shouldReturnEmptyWhenUpdatingNonExisting() {
        Category fake = new Category(null, "Fake");

        Optional<Category> result = service.update(999L, fake);
        assertTrue(result.isEmpty(), "Оновлення неіснуючої категорії має повернути порожній Optional");
    }

    @Test
    void shouldDeleteExistingCategory() {
        Category existing = service.create(new Category(null, "Temp"));
        Long existingId = existing.getId();

        boolean deleted = service.delete(existingId);

        assertTrue(deleted, "Видалення існуючої категорії має повернути true");
        assertEquals(0, service.findAll().size(), "Після видалення сховище має бути порожнім");
        assertTrue(service.findById(existingId).isEmpty(), "Видалену категорію не можна знайти");
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExisting() {
        service.create(new Category(null, "Still Here"));

        boolean deleted = service.delete(999L);

        assertFalse(deleted, "Видалення неіснуючої категорії має повернути false");
        assertEquals(1, service.findAll().size(), "Кількість категорій не має змінитися");
    }
}