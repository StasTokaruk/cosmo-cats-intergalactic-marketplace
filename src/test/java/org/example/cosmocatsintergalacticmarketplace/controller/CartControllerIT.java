package org.example.cosmocatsintergalacticmarketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cosmocatsintergalacticmarketplace.domain.Cart;
import org.example.cosmocatsintergalacticmarketplace.dto.CartDTO;
import org.example.cosmocatsintergalacticmarketplace.service.CartService;
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
 * Integration Test for CartController using real CartService.
 * Covers all CRUD operations and validation scenarios.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CartControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CartService cartService;

    private final String API_URL = "/api/v1/carts";
    private CartDTO validCartDTO;

    @BeforeEach
    void setup() {
        validCartDTO = CartDTO.builder()
                .totalPrice(300.0)
                .products(Collections.emptyList())
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("should create a valid cart and return 201")
    void shouldCreateValidCart() throws Exception {
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCartDTO)))
                .andExpect(status().isCreated())
                // Оскільки mock data ініціалізує 1L і 2L, очікуємо 3L
                .andExpect(header().string("Location", containsString(API_URL + "/3")))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.totalPrice", is(300.0)));
    }

    @Test
    @Order(2)
    @DisplayName("should return 400 Bad Request on invalid data (negative price/null products)")
    void shouldReturnBadRequestOnInvalidData() throws Exception {
        // Порушення: totalPrice має бути @PositiveOrZero; products не має бути @NotNull
        CartDTO invalidDTO = CartDTO.builder()
                .totalPrice(-10.0)
                .products(null)
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest()) // 400 Bad Request
                // Перевірка структури помилки від GlobalExceptionHandler
                .andExpect(jsonPath("$.error.error").value("Bad Request"))
                // Перевіряємо, що є список помилок валідації
                .andExpect(jsonPath("$.invalidParams", not(empty())))
                // Перевіряємо, що є помилка для поля totalPrice
                .andExpect(jsonPath("$.invalidParams[?(@.field == 'totalPrice')]", not(empty())));
    }

    @Test
    @Order(3)
    @DisplayName("should get all existing carts (initial mocks + created)")
    void shouldGetAllCarts() throws Exception {
        // Очікуємо 2 мок-записи + 1, створений у тесті 1
        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))));
    }

    @Test
    @Order(4)
    @DisplayName("should get cart by ID (1L - mock data)")
    void shouldGetCartByIdFound() throws Exception {
        mockMvc.perform(get(API_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @Order(5)
    @DisplayName("should return 404 for non-existent ID")
    void shouldReturnNotFound() throws Exception {
        mockMvc.perform(get(API_URL + "/9999"))
                .andExpect(status().isNotFound());
    }


    @Test
    @Order(6)
    @DisplayName("should delete cart by ID and return 204")
    void shouldDeleteCart() throws Exception {
        // Створюємо новий об'єкт для видалення (ID = 4L)
        cartService.create(new Cart(null, Collections.emptyList(), 50.0));
        Long idToDelete = 4L;

        mockMvc.perform(delete(API_URL + "/" + idToDelete))
                .andExpect(status().isNoContent()); // 204

        // Перевірка, що його дійсно немає
        mockMvc.perform(get(API_URL + "/" + idToDelete))
                .andExpect(status().isNotFound());
    }
}