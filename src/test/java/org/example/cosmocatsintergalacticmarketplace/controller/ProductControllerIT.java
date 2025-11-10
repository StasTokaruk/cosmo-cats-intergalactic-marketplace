package org.example.cosmocatsintergalacticmarketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cosmocatsintergalacticmarketplace.domain.Category;
import org.example.cosmocatsintergalacticmarketplace.domain.Product;
import org.example.cosmocatsintergalacticmarketplace.dto.CategoryDTO;
import org.example.cosmocatsintergalacticmarketplace.dto.ProductDTO;
import org.example.cosmocatsintergalacticmarketplace.service.ProductService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Test for ProductController
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    private ProductDTO validProduct;

    @BeforeEach
    void setup() {
        validProduct = ProductDTO.builder()
                .name("comet")
                .price(500.0)
                .category(CategoryDTO.builder().id(1L).name("Weapons").build())
                .description("Advanced intergalactic blaster")
                .quantity(10.0)
                .build();
    }


    // ✅ 1. Create product - SUCCESS (FINAL FIX)
    @Test
    @Order(1)
    @DisplayName("should create product successfully with valid data")
    void shouldCreateProductSuccessfully() throws Exception {
        System.out.println("Testing with product: " + objectMapper.writeValueAsString(validProduct));

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProduct)))
                .andDo(MockMvcResultHandlers.print()) // Детальний вивід для дебагінгу
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("comet"))
                .andExpect(jsonPath("$.price", is(500.0)))
                .andExpect(jsonPath("$.description").value("Advanced intergalactic blaster"))
                .andExpect(jsonPath("$.quantity", is(10.0)))
                .andExpect(jsonPath("$.category.id", is(1))) // JSON зазвичай перетворює Long у Integer, якщо число невелике
                .andExpect(jsonPath("$.category.name").value("Weapons"));
    }


    // ⚠️ 2. Invalid data
    @Test
    @Order(2)
    @DisplayName("should fail to create product with invalid data (blank name, negative price, null category)")
    void shouldNotCreateInvalidProduct() throws Exception {
        ProductDTO invalid = ProductDTO.builder()
                .name("")
                .price(-5.0)
                .category(null)
                .description("")
                .quantity(-2.0)
                .build();

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", anyOf(
                        hasKey("errors"),
                        hasKey("error")
                )));
    }

    // ✅ 3. Get all (ПОВЕРТАЄ List<ProductAvailabilityDTO>)
    @Test
    @Order(3)
    @DisplayName("should get all products")
    void shouldGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[0].name", notNullValue()));
    }

    // ✅ 4. Get by ID (ПОВЕРТАЄ ProductDetailDTO)
    @Test
    @Order(4)
    @DisplayName("should get product by ID")
    void shouldGetProductById() throws Exception {
        Product product = productService.create(
                new Product(null, "Photon Blade", 750.0,
                        new Category(3L, "Weapons"), "Light-based melee weapon", 7.0));

        mockMvc.perform(get("/api/v1/products/" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Photon Blade"))
                // ProductDetailDTO не має поля 'price'. Перевіряємо 'description'.
                .andExpect(jsonPath("$.description").value("Light-based melee weapon"));
    }

    // ⚠️ 5. Get by ID - Not Found
    @Test
    @Order(5)
    @DisplayName("should return 40 if product not found")
    void shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/products/9999"))
                .andExpect(status().isNotFound());
    }

    // ✅ 6. Update
    @Test
    @Order(6)
    @DisplayName("should update product successfully")
    void shouldUpdateProduct() throws Exception {
        Product product = productService.create(
                new Product(null, "Cosmo Helmet", 200.0,
                        new Category(2L, "Gear"), "Pilot protection", 15.0));

        ProductDTO updated = ProductDTO.builder()
                .name("Cosmo Helmet X")
                .price(220.0)
                .category(CategoryDTO.builder().id(2L).name("Gear").build())
                .description("Updated version")
                .quantity(12.0)
                .build();

        mockMvc.perform(put("/api/v1/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cosmo Helmet X"))
                .andExpect(jsonPath("$.price").value(220.0));
    }

    // ✅ 7. Delete
    @Test
    @Order(7)
    @DisplayName("should delete product")
    void shouldDeleteProduct() throws Exception {
        Product product = productService.create(
                new Product(null, "Space Suit", 400.0,
                        new Category(5L, "Gear"), "Standard EVA suit", 3.0));

        mockMvc.perform(delete("/api/v1/products/" + product.getId()))
                .andExpect(status().isNoContent());
    }
}