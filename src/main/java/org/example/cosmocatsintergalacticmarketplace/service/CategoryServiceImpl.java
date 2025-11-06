package org.example.cosmocatsintergalacticmarketplace.service;

import org.example.cosmocatsintergalacticmarketplace.domain.Category;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final Map<Long, Category> store = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(0);

    @Override
    public Category create(Category category) {
        Long id = idGen.incrementAndGet();
        category.setId(id);
        store.put(id, category);
        return category;
    }

    @Override
    public List<Category> findAll() { return new ArrayList<>(store.values()); }

    @Override
    public Optional<Category> findById(Long id) { return Optional.ofNullable(store.get(id)); }

    @Override
    public Optional<Category> update(Long id, Category category) {
        if (!store.containsKey(id)) return Optional.empty();
        category.setId(id);
        store.put(id, category);
        return Optional.of(category);
    }

    @Override
    public boolean delete(Long id) { return store.remove(id) != null; }
}
