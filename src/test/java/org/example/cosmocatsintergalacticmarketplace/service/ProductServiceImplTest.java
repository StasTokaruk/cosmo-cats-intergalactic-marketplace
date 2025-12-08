package org.example.cosmocatsintergalacticmarketplace.service;

import org.example.cosmocatsintergalacticmarketplace.domain.Category;
import org.example.cosmocatsintergalacticmarketplace.domain.Product;
import org.example.cosmocatsintergalacticmarketplace.mapper.ProductMapper;
import org.example.cosmocatsintergalacticmarketplace.repositories.ProductRepository;
import org.example.cosmocatsintergalacticmarketplace.repositories.entity.CategoryEntity;
import org.example.cosmocatsintergalacticmarketplace.repositories.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    void shouldFindAllProducts() {
        // GIVEN
        ProductEntity entity = new ProductEntity(1L, "Test Entity", BigDecimal.TEN, "Desc", 5, new CategoryEntity());
        Product domain = new Product(1L, "Test Domain", BigDecimal.TEN, new Category(), "Desc", 5);

        when(productRepository.findAll()).thenReturn(List.of(entity));
        when(productMapper.toDomain(entity)).thenReturn(domain);

        // WHEN
        List<Product> result = service.findAll();

        // THEN
        assertEquals(1, result.size());
        assertEquals("Test Domain", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void shouldCreateNewProduct() {
        // GIVEN
        Product inputProduct = new Product(null, "Cosmo Mug", BigDecimal.valueOf(15.5),
                new Category(3L, "Souvenirs"), "For intergalactic tea", 100);

        ProductEntity mappedEntity = new ProductEntity(null, "Cosmo Mug", BigDecimal.valueOf(15.5), "For intergalactic tea", 100, null);
        ProductEntity savedEntity = new ProductEntity(1L, "Cosmo Mug", BigDecimal.valueOf(15.5), "For intergalactic tea", 100, null);
        Product resultDomain = new Product(1L, "Cosmo Mug", BigDecimal.valueOf(15.5), new Category(3L, "Souvenirs"), "For intergalactic tea", 100);

        when(productMapper.toEntity(inputProduct)).thenReturn(mappedEntity);
        when(productRepository.save(mappedEntity)).thenReturn(savedEntity);
        when(productMapper.toDomain(savedEntity)).thenReturn(resultDomain);

        // WHEN
        Product created = service.create(inputProduct);

        // THEN
        assertNotNull(created.getId());
        assertEquals(1L, created.getId());
        verify(productRepository).save(any(ProductEntity.class));
    }

    @Test
    void shouldFindProductById() {
        // GIVEN
        Long id = 1L;
        ProductEntity entity = new ProductEntity(id, "Laser Sword", BigDecimal.valueOf(999), "Weapon", 5, null);
        Product domain = new Product(id, "Laser Sword", BigDecimal.valueOf(999), null, "Weapon", 5);

        when(productRepository.findById(id)).thenReturn(Optional.of(entity));
        when(productMapper.toDomain(entity)).thenReturn(domain);

        // WHEN
        Optional<Product> found = service.findById(id);

        // THEN
        assertTrue(found.isPresent());
        assertEquals("Laser Sword", found.get().getName());
    }

    @Test
    void shouldReturnEmptyIfNotFound() {
        // GIVEN
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN
        Optional<Product> found = service.findById(99L);

        // THEN
        assertTrue(found.isEmpty());
    }

    @Test
    void shouldUpdateExistingProduct() {
        // GIVEN
        Long id = 1L;
        Product updateInfo = new Product(null, "Updated Name", BigDecimal.valueOf(200), null, "Desc", 10);

        ProductEntity existingEntity = new ProductEntity(id, "Old Name", BigDecimal.valueOf(100), "Desc", 5, null);

        ProductEntity savedEntity = new ProductEntity(id, "Updated Name", BigDecimal.valueOf(200), "Desc", 10, null);
        Product updatedDomain = new Product(id, "Updated Name", BigDecimal.valueOf(200), null, "Desc", 10);

        when(productRepository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(productRepository.save(existingEntity)).thenReturn(savedEntity);
        when(productMapper.toDomain(savedEntity)).thenReturn(updatedDomain);

        // WHEN
        Optional<Product> result = service.update(id, updateInfo);

        // THEN
        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
        verify(productRepository).save(existingEntity);
    }

    @Test
    void shouldReturnEmptyWhenUpdatingNonExisting() {
        // GIVEN
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        Product fake = new Product(null, "Fake", BigDecimal.TEN, null, "Desc", 1);

        // WHEN
        Optional<Product> result = service.update(999L, fake);

        // THEN
        assertTrue(result.isEmpty());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldDeleteExistingProduct() {
        // GIVEN
        when(productRepository.existsById(2L)).thenReturn(true);

        // WHEN
        boolean deleted = service.delete(2L);

        // THEN
        assertTrue(deleted);
        verify(productRepository).deleteById(2L);
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExisting() {
        // GIVEN
        when(productRepository.existsById(999L)).thenReturn(false);

        // WHEN
        boolean deleted = service.delete(999L);

        // THEN
        assertFalse(deleted);
        verify(productRepository, never()).deleteById(any());
    }
}