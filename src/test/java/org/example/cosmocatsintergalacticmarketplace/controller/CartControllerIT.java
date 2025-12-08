package org.example.cosmocatsintergalacticmarketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cosmocatsintergalacticmarketplace.AbstractIntegrationTest;
import org.example.cosmocatsintergalacticmarketplace.domain.Cart;
import org.example.cosmocatsintergalacticmarketplace.dto.CartDTO;
import org.example.cosmocatsintergalacticmarketplace.service.CartService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CartControllerIT extends AbstractIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private CartService cartService;

    private CartDTO validCartDTO;

    @BeforeEach
    void setup() {
        validCartDTO = CartDTO.builder()
                .totalPrice(BigDecimal.valueOf(300))
                .products(Collections.emptyList())
                .build();
    }

    @Test
    void shouldCreateValidCart() throws Exception {
        mockMvc.perform(post("/api/v1/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCartDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void shouldGetAllCarts() throws Exception {
        cartService.create(new Cart(null, new ArrayList<>(), BigDecimal.TEN));

        mockMvc.perform(get("/api/v1/carts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldGetCartById() throws Exception {
        Cart cart = cartService.create(new Cart(null, new ArrayList<>(), BigDecimal.valueOf(100)));

        mockMvc.perform(get("/api/v1/carts/" + cart.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cart.getId()));
    }

    @Test
    void shouldDeleteCart() throws Exception {
        Cart cart = cartService.create(new Cart(null, new ArrayList<>(), BigDecimal.valueOf(50)));

        mockMvc.perform(delete("/api/v1/carts/" + cart.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/carts/" + cart.getId()))
                .andExpect(status().isNotFound());
    }
}