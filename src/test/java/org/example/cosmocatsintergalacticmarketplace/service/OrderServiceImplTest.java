package org.example.cosmocatsintergalacticmarketplace.service;

import org.example.cosmocatsintergalacticmarketplace.domain.Order;
import org.example.cosmocatsintergalacticmarketplace.mapper.OrderMapper;
import org.example.cosmocatsintergalacticmarketplace.repositories.OrderRepository;
import org.example.cosmocatsintergalacticmarketplace.repositories.entity.OrderEntity;
import org.example.cosmocatsintergalacticmarketplace.repositories.entity.OrderItemEntity;
import org.example.cosmocatsintergalacticmarketplace.repositories.projection.TopProductProjection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl service;

    // Константи для зручності
    private static final List<OrderItemEntity> EMPTY_ENTITY_ITEMS = new ArrayList<>();
    private static final List<org.example.cosmocatsintergalacticmarketplace.domain.Product> EMPTY_DOMAIN_ITEMS = new ArrayList<>();

    @Test
    void shouldFindAllOrders() {
        // GIVEN
        OrderEntity entity = new OrderEntity(1L, "ORD-123", BigDecimal.TEN, null, EMPTY_ENTITY_ITEMS);
        Order domain = new Order(1L, EMPTY_DOMAIN_ITEMS, BigDecimal.TEN);

        when(orderRepository.findAll()).thenReturn(List.of(entity));
        when(orderMapper.toDomain(entity)).thenReturn(domain);

        // WHEN
        List<Order> orders = service.findAll();

        // THEN
        assertEquals(1, orders.size());
        assertEquals(BigDecimal.TEN, orders.get(0).getTotalPrice());
        verify(orderRepository).findAll();
    }

    @Test
    void shouldFindOrderById() {
        // GIVEN
        Long id = 1L;
        OrderEntity entity = new OrderEntity(id, "ORD-1", BigDecimal.ONE, null, EMPTY_ENTITY_ITEMS);
        Order domain = new Order(id, EMPTY_DOMAIN_ITEMS, BigDecimal.ONE);

        when(orderRepository.findById(id)).thenReturn(Optional.of(entity));
        when(orderMapper.toDomain(entity)).thenReturn(domain);

        // WHEN
        Optional<Order> found = service.findById(id);

        // THEN
        assertTrue(found.isPresent());
        assertEquals(id, found.get().getId());
    }

    @Test
    void shouldReturnEmptyIfNotFound() {
        // GIVEN
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN
        Optional<Order> found = service.findById(99L);

        // THEN
        assertTrue(found.isEmpty());
    }

    @Test
    void shouldCreateNewOrder() {
        // GIVEN
        Order newOrder = new Order(null, EMPTY_DOMAIN_ITEMS, BigDecimal.valueOf(50.0));

        OrderEntity entityToSave = new OrderEntity(null, null, BigDecimal.valueOf(50.0), null, EMPTY_ENTITY_ITEMS);

        OrderEntity savedEntity = new OrderEntity(1L, "ORD-GENERATED", BigDecimal.valueOf(50.0), null, EMPTY_ENTITY_ITEMS);

        Order savedDomain = new Order(1L, EMPTY_DOMAIN_ITEMS, BigDecimal.valueOf(50.0));

        when(orderMapper.toEntity(newOrder)).thenReturn(entityToSave);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedEntity);
        when(orderMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        // WHEN
        Order created = service.create(newOrder);

        // THEN
        assertNotNull(created.getId());
        assertEquals(1L, created.getId());
        verify(orderRepository).save(any(OrderEntity.class));
    }

    @Test
    void shouldUpdateExistingOrder() {
        // GIVEN
        Long id = 1L;
        Order updateInfo = new Order(null, EMPTY_DOMAIN_ITEMS, BigDecimal.valueOf(150.0));

        OrderEntity existingEntity = new OrderEntity(id, "ORD-1", BigDecimal.valueOf(50.0), null, EMPTY_ENTITY_ITEMS);
        OrderEntity updatedEntity = new OrderEntity(id, "ORD-1", BigDecimal.valueOf(150.0), null, EMPTY_ENTITY_ITEMS);
        Order domainResult = new Order(id, EMPTY_DOMAIN_ITEMS, BigDecimal.valueOf(150.0));

        when(orderRepository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(orderRepository.save(existingEntity)).thenReturn(updatedEntity);
        when(orderMapper.toDomain(updatedEntity)).thenReturn(domainResult);

        // WHEN
        Optional<Order> result = service.update(id, updateInfo);

        // THEN
        assertTrue(result.isPresent());
        assertEquals(BigDecimal.valueOf(150.0), result.get().getTotalPrice());
        verify(orderRepository).save(existingEntity);
    }

    @Test
    void shouldReturnEmptyWhenUpdatingNonExisting() {
        // GIVEN
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());
        Order fake = new Order(null, EMPTY_DOMAIN_ITEMS, BigDecimal.TEN);

        // WHEN
        Optional<Order> result = service.update(999L, fake);

        // THEN
        assertTrue(result.isEmpty());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldDeleteExistingOrder() {
        // GIVEN
        when(orderRepository.existsById(2L)).thenReturn(true);

        // WHEN
        boolean deleted = service.delete(2L);

        // THEN
        assertTrue(deleted);
        verify(orderRepository).deleteById(2L);
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExisting() {
        // GIVEN
        when(orderRepository.existsById(999L)).thenReturn(false);

        // WHEN
        boolean deleted = service.delete(999L);

        // THEN
        assertFalse(deleted);
        verify(orderRepository, never()).deleteById(any());
    }
}