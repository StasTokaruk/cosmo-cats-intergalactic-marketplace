package org.example.cosmocatsintergalacticmarketplace.service;

import org.example.cosmocatsintergalacticmarketplace.domain.Cart;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CartServiceImpl implements CartService {

    private final Map<Long, Cart> store = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(0);

    @PostConstruct
    private void initMockData() {
        store.put(1L, new Cart(1L, new ArrayList<>(), 0.0));
        store.put(2L, new Cart(2L, new ArrayList<>(), 0.0));
        idGen.set(2);
    }

    private Long nextId() { return idGen.incrementAndGet(); }

    @Override
    public Cart create(Cart cart) {
        Long id = nextId();
        cart.setId(id);
        store.put(id, cart);
        return cart;
    }

    @Override
    public List<Cart> findAll() { return new ArrayList<>(store.values()); }

    @Override
    public Optional<Cart> findById(Long id) { return Optional.ofNullable(store.get(id)); }

    @Override
    public Optional<Cart> update(Long id, Cart cart) {
        if (!store.containsKey(id)) return Optional.empty();
        cart.setId(id);
        store.put(id, cart);
        return Optional.of(cart);
    }

    @Override
    public boolean delete(Long id) { return store.remove(id) != null; }
}
