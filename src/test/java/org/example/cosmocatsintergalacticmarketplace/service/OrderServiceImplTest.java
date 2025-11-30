package org.example.cosmocatsintergalacticmarketplace.service;

import org.example.cosmocatsintergalacticmarketplace.domain.Order;
import org.example.cosmocatsintergalacticmarketplace.domain.Product; // Припускаємо, що Order містить список Product
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {

    private OrderServiceImpl service;

    private static final List<Product> EMPTY_ITEMS = new ArrayList<>();

    @BeforeEach
    void setUp() {
        service = new OrderServiceImpl();
        service.initMockData();
    }

    @Test
    void shouldInitializeWithMockData() {
        List<Order> orders = service.findAll();
        assertEquals(2, orders.size(), "Після ініціалізації має бути 2 замовлення");
    }

    @Test
    void shouldFindOrderById() {
        Optional<Order> found = service.findById(1L);

        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
    }

    @Test
    void shouldReturnEmptyIfNotFound() {
        Optional<Order> found = service.findById(99L);
        assertTrue(found.isEmpty());
    }

    @Test
    void shouldCreateNewOrder() {
        Order newOrder = new Order(null, EMPTY_ITEMS, BigDecimal.valueOf(50.0));

        Order created = service.create(newOrder);

        assertNotNull(created.getId(), "Створене замовлення має отримати ID");
        assertEquals(3, service.findAll().size(), "Має бути 3 замовлення після створення");
    }

    @Test
    void shouldGenerateSequentialIds() {
        Order first = service.create(new Order(null, EMPTY_ITEMS, BigDecimal.valueOf(5.0)));
        Order second = service.create(new Order(null, EMPTY_ITEMS, BigDecimal.valueOf(2.0)));

        assertTrue(second.getId() > first.getId(), "Наступний ID має бути більшим за попередній");
    }


    @Test
    void shouldUpdateExistingOrder() {
        Order update = new Order(null, EMPTY_ITEMS, BigDecimal.valueOf(150.75));

        Optional<Order> result = service.update(1L, update);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId(), "ID оновленого замовлення має збігатися");
    }

    @Test
    void shouldReturnEmptyWhenUpdatingNonExisting() {
        Order fake = new Order(null, EMPTY_ITEMS, BigDecimal.TEN);

        Optional<Order> result = service.update(999L, fake);
        assertTrue(result.isEmpty(), "Оновлення неіснуючого замовлення має повернути порожній Optional");
    }

    @Test
    void shouldDeleteExistingOrder() {
        boolean deleted = service.delete(2L);

        assertTrue(deleted, "Видалення існуючого замовлення має повернути true");
        assertEquals(1, service.findAll().size(), "Після видалення має залишитися 1 замовлення");
        assertTrue(service.findById(2L).isEmpty(), "Видалене замовлення не можна знайти");
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExisting() {
        boolean deleted = service.delete(999L);

        assertFalse(deleted, "Видалення неіснуючого замовлення має повернути false");
        assertEquals(2, service.findAll().size(), "Кількість замовлень не має змінитися");
    }
}