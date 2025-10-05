package org.example.cosmocatsintergalacticmarketplace.service;

import org.example.cosmocatsintergalacticmarketplace.domain.Product;
import org.example.cosmocatsintergalacticmarketplace.domain.Category;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductServiceImpl implements ProductService {

    private final Map<Long, Product> store = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(0);

    @PostConstruct
    private void initMockData() {
        store.put(1L, new Product(1L, "Laser Sword", 999.0,
                new Category(1L, "Electronics"), "Space weapon", 5.0));
        store.put(2L, new Product(2L, "Cosmo Book", 49.0,
                new Category(2L, "Books"), "Manual for pilots", 20.0));
        idGen.set(2);
    }

    private Long nextId() { return idGen.incrementAndGet(); }

    @Override
    public Product create(Product product) {
        Long id = nextId();
        product.setId(id);
        store.put(id, product);
        return product;
    }

    @Override
    public List<Product> findAll() { return new ArrayList<>(store.values()); }

    @Override
    public Optional<Product> findById(Long id) { return Optional.ofNullable(store.get(id)); }

    @Override
    public Optional<Product> update(Long id, Product product) {
        if (!store.containsKey(id)) return Optional.empty();
        product.setId(id);
        store.put(id, product);
        return Optional.of(product);
    }

    @Override
    public boolean delete(Long id) { return store.remove(id) != null; }
}
