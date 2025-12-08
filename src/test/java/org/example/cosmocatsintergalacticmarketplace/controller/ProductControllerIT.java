package org.example.cosmocatsintergalacticmarketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cosmocatsintergalacticmarketplace.AbstractIntegrationTest;
import org.example.cosmocatsintergalacticmarketplace.domain.Category;
import org.example.cosmocatsintergalacticmarketplace.domain.Product;
import org.example.cosmocatsintergalacticmarketplace.dto.CategoryDTO;
import org.example.cosmocatsintergalacticmarketplace.dto.ProductDTO;
import org.example.cosmocatsintergalacticmarketplace.service.CategoryService;
import org.example.cosmocatsintergalacticmarketplace.service.ProductService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerIT extends AbstractIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ProductService productService;
    @Autowired private CategoryService categoryService;

    private ProductDTO validProductDTO;
    private Category savedCategory;

    @BeforeEach
    void setup() {
        savedCategory = categoryService.create(new Category(null, "Weapons"));

        validProductDTO = ProductDTO.builder()
                .name("CosmoBlaster")
                .price(BigDecimal.valueOf(500))
                .category(CategoryDTO.builder().id(savedCategory.getId()).name("Weapons").build())
                .description("Powerful blaster")
                .quantity(10)
                .build();
    }

    @Test
    void shouldCreateProductSuccessfully() throws Exception {
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProductDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void shouldGetAllProducts() throws Exception {
        // Додаємо продукт, щоб список не був пустим
        productService.create(new Product(null, "TempProd", BigDecimal.TEN, savedCategory, "Desc", 5));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    void shouldGetProductById() throws Exception {
        Product created = productService.create(new Product(null, "FindMe", BigDecimal.valueOf(100), savedCategory, "Desc", 1));

        mockMvc.perform(get("/api/v1/products/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("FindMe"));
    }

    @Test
    void shouldReturnNotFoundForNonExistentProduct() throws Exception {
        mockMvc.perform(get("/api/v1/products/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        Product existing = productService.create(new Product(null, "OldName", BigDecimal.valueOf(50), savedCategory, "OldDesc", 1));

        ProductDTO updateDTO = ProductDTO.builder()
                .name("NewCosmoName")
                .price(BigDecimal.valueOf(999))
                .category(CategoryDTO.builder().id(savedCategory.getId()).name("Weapons").build())
                .description("New Description")
                .quantity(20)
                .build();

        mockMvc.perform(put("/api/v1/products/" + existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NewCosmoName"))
                .andExpect(jsonPath("$.price").value(999));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        Product toDelete = productService.create(new Product(null, "DeleteMe", BigDecimal.TEN, savedCategory, "Desc", 1));

        mockMvc.perform(delete("/api/v1/products/" + toDelete.getId()))
                .andExpect(status().isNoContent()); // 204

        mockMvc.perform(get("/api/v1/products/" + toDelete.getId()))
                .andExpect(status().isNotFound());
    }
}