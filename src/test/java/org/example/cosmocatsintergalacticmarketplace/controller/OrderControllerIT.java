package org.example.cosmocatsintergalacticmarketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cosmocatsintergalacticmarketplace.domain.Order;
import org.example.cosmocatsintergalacticmarketplace.dto.OrderDTO;
import org.example.cosmocatsintergalacticmarketplace.service.OrderService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private OrderService orderService;

    private OrderDTO validOrderDTO;

    @BeforeEach
    void setup() {
        validOrderDTO = OrderDTO.builder()
                .totalPrice(BigDecimal.valueOf(300))
                .products(Collections.emptyList())
                .build();
    }

    @Test
    void shouldCreateValidOrder() throws Exception {
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validOrderDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }


    @Test
    void shouldGetAllOrders() throws Exception {
        orderService.create(new Order(null, Collections.emptyList(), BigDecimal.TEN));

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    void shouldGetOrderById() throws Exception {
        Order order = orderService.create(new Order(null, Collections.emptyList(), BigDecimal.valueOf(50)));

        mockMvc.perform(get("/api/v1/orders/" + order.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()));
    }

    @Test
    void shouldReturnNotFoundForNonExistentOrder() throws Exception {
        mockMvc.perform(get("/api/v1/orders/999999"))
                .andExpect(status().isNotFound());
    }


    @Test
    void shouldDeleteOrder() throws Exception {
        Order order = orderService.create(new Order(null, Collections.emptyList(), BigDecimal.valueOf(50)));

        mockMvc.perform(delete("/api/v1/orders/" + order.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/orders/" + order.getId()))
                .andExpect(status().isNotFound());
    }


    @Test
    void shouldGetTopProductsReport() throws Exception {
        // Навіть якщо немає продажів, метод має повертати 200 OK і порожній список (або заповнений)
        mockMvc.perform(get("/api/v1/orders/top-products"))
                .andExpect(status().isOk());
    }
}