package org.example.cosmocatsintergalacticmarketplace.service;

import org.example.cosmocatsintergalacticmarketplace.domain.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order create(Order order);
    List<Order> findAll();
    Optional<Order> findById(Long id);
    Optional<Order> update(Long id, Order order);
    boolean delete(Long id);
}
