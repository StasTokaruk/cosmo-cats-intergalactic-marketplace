package org.example.cosmocatsintergalacticmarketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cosmocatsintergalacticmarketplace.dto.OrderDTO;
import org.example.cosmocatsintergalacticmarketplace.service.OrderService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Collections;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Test for OrderController using real OrderService
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderService orderService; // Використовуємо реальний сервіс для CRUD

    private OrderDTO validOrderDTO;

    @BeforeEach
    void setup() {
        // Скидаємо сервіс до початкового стану, щоб ID не перекривалися (якщо це можливо)
        // Для in-memory Map це не завжди просто, але ми покладаємося на @Order
        // та на те, що сервіс має мок-дані з 1L та 2L.
        validOrderDTO = OrderDTO.builder()
                .totalPrice(300.0)
                .products(Collections.emptyList())
                .build();
    }

    // --- 1. POST (CREATE) - Позитивний сценарій (ID = 3L, оскільки 1L, 2L вже є мок-даними) ---
    @Test
    @Order(1)
    @DisplayName("should create a valid order and return 201")
    void shouldCreateValidOrder() throws Exception {
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validOrderDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/v1/orders/3")))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.totalPrice", is(300.0)));
    }

    // --- 2. POST (CREATE) - Негативний сценарій (Валідація) ---
// OrderControllerIT.java (Виправлений фрагмент)
    @Test
    @Order(2)
    @DisplayName("should return 400 Bad Request on invalid data (e.g., negative price)")
    void shouldReturnBadRequestOnInvalidData() throws Exception {
        OrderDTO invalidDTO = OrderDTO.builder()
                .totalPrice(-10.0)
                .products(null)
                .build();

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.error", notNullValue()))
                .andExpect(jsonPath("$.invalidParams", not(empty())))
                .andExpect(jsonPath("$.invalidParams[?(@.field == 'totalPrice')]", not(empty())));
    }

    // --- 3. GET ALL ---
    @Test
    @Order(3)
    @DisplayName("should get all existing orders")
    void shouldGetAllOrders() throws Exception {
        // Очікуємо 2 мок-записи + 1, створений у тесті 1
        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))));
    }

    // --- 4. GET BY ID - Found ---
    @Test
    @Order(4)
    @DisplayName("should get order by ID (1L - mock data)")
    void shouldGetOrderByIdFound() throws Exception {
        mockMvc.perform(get("/api/v1/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // --- 5. GET BY ID - Not Found ---
    @Test
    @Order(5)
    @DisplayName("should return 404 for non-existent ID")
    void shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/orders/9999"))
                .andExpect(status().isNotFound());
    }

    // --- 6. DELETE ---
    @Test
    @Order(6)
    @DisplayName("should delete order by ID and return 204")
    void shouldDeleteOrder() throws Exception {
        // Створюємо новий об'єкт для видалення, щоб не залежати від порядку тестів
        orderService.create(new org.example.cosmocatsintergalacticmarketplace.domain.Order(null, Collections.emptyList(), 50.0));
        Long idToDelete = 4L; // Наступний ID

        mockMvc.perform(delete("/api/v1/orders/" + idToDelete))
                .andExpect(status().isNoContent()); // 204

        // Перевірка, що його дійсно немає
        mockMvc.perform(get("/api/v1/orders/" + idToDelete))
                .andExpect(status().isNotFound());
    }
}