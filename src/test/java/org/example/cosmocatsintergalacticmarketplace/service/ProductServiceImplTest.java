package org.example.cosmocatsintergalacticmarketplace.service;

import org.example.cosmocatsintergalacticmarketplace.domain.Category;
import org.example.cosmocatsintergalacticmarketplace.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceImplTest {

    private ProductServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProductServiceImpl();
        service.initMockData();
    }

    @Test
    void shouldInitializeWithMockData() {
        List<Product> products = service.findAll();
        assertEquals(2, products.size(), "Після ініціалізації має бути 2 продукти");
    }

    @Test
    void shouldCreateNewProduct() {
        Product product = new Product(null, "Cosmo Mug", 15.5,
                new Category(3L, "Souvenirs"), "For intergalactic tea", 100.0);

        Product created = service.create(product);

        assertNotNull(created.getId());
        assertEquals(3, service.findAll().size(), "Має бути 3 продукти після створення");
    }

    @Test
    void shouldFindProductById() {
        Optional<Product> found = service.findById(1L);

        assertTrue(found.isPresent());
        assertEquals("Laser Sword", found.get().getName());
    }

    @Test
    void shouldReturnEmptyIfNotFound() {
        Optional<Product> found = service.findById(99L);
        assertTrue(found.isEmpty());
    }

    @Test
    void shouldUpdateExistingProduct() {
        Product update = new Product(null, "Laser Sword MK2", 1200.0,
                new Category(1L, "Electronics"), "Upgraded", 10.0);

        Optional<Product> result = service.update(1L, update);

        assertTrue(result.isPresent());
        assertEquals("Laser Sword MK2", result.get().getName());
        assertEquals(1200.0, result.get().getPrice());
    }

    @Test
    void shouldReturnEmptyWhenUpdatingNonExisting() {
        Product fake = new Product(null, "Fake", 10.0,
                new Category(99L, "FakeCat"), "Nope", 0.0);

        Optional<Product> result = service.update(999L, fake);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldDeleteExistingProduct() {
        boolean deleted = service.delete(2L);

        assertTrue(deleted);
        assertEquals(1, service.findAll().size());
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExisting() {
        boolean deleted = service.delete(999L);

        assertFalse(deleted);
    }

    @Test
    void shouldGenerateSequentialIds() {
        Product first = service.create(new Product(null, "Item A", 1.0,
                new Category(4L, "Misc"), "desc", 5.0));
        Product second = service.create(new Product(null, "Item B", 2.0,
                new Category(4L, "Misc"), "desc", 5.0));

        assertTrue(second.getId() > first.getId());
    }
}
