package org.example.cosmocatsintergalacticmarketplace.service;

import org.example.cosmocatsintergalacticmarketplace.domain.Order;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderServiceImpl implements OrderService {

    private final Map<Long, Order> store = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(0);

    @PostConstruct
    private void initMockData() {
        store.put(1L, new Order(1L, new ArrayList<>(), 0.0));
        store.put(2L, new Order(2L, new ArrayList<>(), 0.0));
        idGen.set(2);
    }

    private Long nextId() { return idGen.incrementAndGet(); }

    @Override
    public Order create(Order order) {
        Long id = nextId();
        order.setId(id);
        store.put(id, order);
        return order;
    }

    @Override
    public List<Order> findAll() { return new ArrayList<>(store.values()); }

    @Override
    public Optional<Order> findById(Long id) { return Optional.ofNullable(store.get(id)); }

    @Override
    public Optional<Order> update(Long id, Order order) {
        if (!store.containsKey(id)) return Optional.empty();
        order.setId(id);
        store.put(id, order);
        return Optional.of(order);
    }

    @Override
    public boolean delete(Long id) { return store.remove(id) != null; }
}
