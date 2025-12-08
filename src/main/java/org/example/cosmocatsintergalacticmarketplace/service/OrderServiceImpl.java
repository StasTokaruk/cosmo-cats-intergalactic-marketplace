package org.example.cosmocatsintergalacticmarketplace.service;

import lombok.RequiredArgsConstructor;
import org.example.cosmocatsintergalacticmarketplace.domain.Order;
import org.example.cosmocatsintergalacticmarketplace.mapper.OrderMapper;
import org.example.cosmocatsintergalacticmarketplace.repositories.OrderRepository;
import org.example.cosmocatsintergalacticmarketplace.repositories.entity.OrderEntity;
import org.example.cosmocatsintergalacticmarketplace.repositories.projection.TopProductProjection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public Order create(Order order) {
        OrderEntity entity = orderMapper.toEntity(order);


        if (entity.getOrderNumber() == null) {
            entity.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        entity.setCreatedAt(LocalDateTime.now());

        if (entity.getItems() != null) {
            entity.getItems().forEach(item -> item.setOrder(entity));
        }

        OrderEntity savedEntity = orderRepository.save(entity);
        return orderMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDomain);
    }

    // Додатковий метод для пошуку за Natural ID
    @Transactional(readOnly = true)
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .map(orderMapper::toDomain);
    }

    @Override
    @Transactional
    public Optional<Order> update(Long id, Order order) {
        return orderRepository.findById(id)
                .map(existingEntity -> {
                    existingEntity.setTotalPrice(order.getTotalPrice());

                    return orderRepository.save(existingEntity);
                })
                .map(orderMapper::toDomain);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }


    @Override
    @Transactional(readOnly = true)
    public List<TopProductProjection> getTopSellingProducts() {
        return orderRepository.findTopSellingProducts();
    }
}