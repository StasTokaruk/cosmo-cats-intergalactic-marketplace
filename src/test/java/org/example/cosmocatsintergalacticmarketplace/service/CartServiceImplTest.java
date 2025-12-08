package org.example.cosmocatsintergalacticmarketplace.service;

import org.example.cosmocatsintergalacticmarketplace.domain.Cart;
import org.example.cosmocatsintergalacticmarketplace.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceImplTest {

    private CartServiceImpl service;

    private static final List<Product> EMPTY_ITEMS = new ArrayList<>();

    @BeforeEach
    void setUp() {
        service = new CartServiceImpl();
        service.initMockData();
    }


    @Test
    void shouldInitializeWithMockData() {
        List<Cart> carts = service.findAll();
        assertEquals(2, carts.size(), "Після ініціалізації має бути 2 кошики");
    }

    @Test
    void shouldFindCartById() {
        Optional<Cart> found = service.findById(1L);

        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
    }

    @Test
    void shouldReturnEmptyIfNotFound() {
        Optional<Cart> found = service.findById(99L);
        assertTrue(found.isEmpty());
    }


    @Test
    void shouldCreateNewCart() {
        Cart newCart = new Cart(null, EMPTY_ITEMS, BigDecimal.ZERO);

        Cart created = service.create(newCart);

        assertNotNull(created.getId());
        assertEquals(3, service.findAll().size(), "Має бути 3 кошики після створення");
    }

    @Test
    void shouldGenerateSequentialIds() {
        Cart first = service.create(new Cart(null, EMPTY_ITEMS, BigDecimal.ZERO));
        Cart second = service.create(new Cart(null, EMPTY_ITEMS, BigDecimal.ZERO));

        assertTrue(second.getId() > first.getId(), "Наступний ID має бути більшим за попередній");
    }

    @Test
    void shouldUpdateExistingCart() {
        Cart update = new Cart(null, EMPTY_ITEMS, BigDecimal.valueOf(99.99));

        Optional<Cart> result = service.update(1L, update);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId(), "ID оновленого кошика має збігатися");
    }

    @Test
    void shouldReturnEmptyWhenUpdatingNonExisting() {
        Cart fake = new Cart(null, EMPTY_ITEMS, BigDecimal.ONE);

        Optional<Cart> result = service.update(999L, fake);
        assertTrue(result.isEmpty(), "Оновлення неіснуючого кошика має повернути порожній Optional");
    }

    @Test
    void shouldDeleteExistingCart() {
        boolean deleted = service.delete(2L);

        assertTrue(deleted, "Видалення існуючого кошика має повернути true");
        assertEquals(1, service.findAll().size(), "Після видалення має залишитися 1 кошик");
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExisting() {
        boolean deleted = service.delete(999L);

        assertFalse(deleted, "Видалення неіснуючого кошика має повернути false");
        assertEquals(2, service.findAll().size(), "Кількість кошиків не має змінитися");
    }
}