package org.example.cosmocatsintergalacticmarketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cosmocatsintergalacticmarketplace.domain.Cart;
import org.example.cosmocatsintergalacticmarketplace.dto.CartDTO;
import org.example.cosmocatsintergalacticmarketplace.mapper.CartMapper;
import org.example.cosmocatsintergalacticmarketplace.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private CartMapper cartMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Cart cart;
    private CartDTO cartDTO;

    @BeforeEach
    void setUp() {
        cart = Cart.builder()
                .id(1L)
                .totalPrice(300.0)
                .products(List.of())
                .build();

        cartDTO = CartDTO.builder()
                .id(1L)
                .totalPrice(300.0)
                .products(List.of())
                .build();
    }

    @Test
    void testGetAllCarts() throws Exception {
        when(cartService.findAll()).thenReturn(List.of(cart));
        when(cartMapper.toCartDTO(cart)).thenReturn(cartDTO);

        mockMvc.perform(get("/api/v1/carts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].totalPrice").value(300.0));
    }

    @Test
    void testGetCartById_Found() throws Exception {
        when(cartService.findById(1L)).thenReturn(Optional.of(cart));
        when(cartMapper.toCartDTO(cart)).thenReturn(cartDTO);

        mockMvc.perform(get("/api/v1/carts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalPrice").value(300.0));
    }

    @Test
    void testGetCartById_NotFound() throws Exception {
        when(cartService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/carts/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateCart() throws Exception {
        Cart inputCart = Cart.builder().totalPrice(400.0).products(List.of()).build();
        CartDTO inputDTO = CartDTO.builder().totalPrice(400.0).products(List.of()).build();
        Cart savedCart = Cart.builder().id(5L).totalPrice(400.0).products(List.of()).build();
        CartDTO savedDTO = CartDTO.builder().id(5L).totalPrice(400.0).products(List.of()).build();

        when(cartMapper.toCartDomain(ArgumentMatchers.any(CartDTO.class))).thenReturn(inputCart);
        when(cartService.create(inputCart)).thenReturn(savedCart);
        when(cartMapper.toCartDTO(savedCart)).thenReturn(savedDTO);

        mockMvc.perform(post("/api/v1/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/api/v1/carts/5")))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.totalPrice").value(400.0));
    }

    @Test
    void testDeleteCart() throws Exception {
        when(cartService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/carts/1"))
                .andExpect(status().isNoContent());

        verify(cartService, times(1)).delete(1L);
    }
}
