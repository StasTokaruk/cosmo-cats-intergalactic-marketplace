package org.example.cosmocatsintergalacticmarketplace.service;

import lombok.RequiredArgsConstructor;
import org.example.cosmocatsintergalacticmarketplace.domain.Product;
import org.example.cosmocatsintergalacticmarketplace.mapper.ProductMapper;
import org.example.cosmocatsintergalacticmarketplace.repositories.ProductRepository;
import org.example.cosmocatsintergalacticmarketplace.repositories.entity.ProductEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional // Відкриває транзакцію для запису
    public Product create(Product product) {
        // 1. Конвертуємо Domain -> Entity
        ProductEntity entity = productMapper.toEntity(product);

        // 2. Зберігаємо в базу (id згенерується автоматично)
        ProductEntity savedEntity = productRepository.save(entity);

        // 3. Конвертуємо назад Entity -> Domain і повертаємо
        return productMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true) // Оптимізація для читання
    public List<Product> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toDomain) // Entity -> Domain
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDomain);
    }

    @Override
    @Transactional
    public Optional<Product> update(Long id, Product product) {
        // Спочатку шукаємо, чи існує продукт
        return productRepository.findById(id)
                .map(existingEntity -> {
                    // Оновлюємо поля знайденої сутності даними з Domain об'єкта

                    existingEntity.setName(product.getName());
                    existingEntity.setPrice(product.getPrice());
                    existingEntity.setDescription(product.getDescription());

                    // У вас в Domain quantity це Double, а в Entity це Integer.
                    // Потрібне приведення типів
                    if (product.getQuantity() != null) {
                        existingEntity.setQuantity(product.getQuantity().intValue());
                    }

                    // Оновлення категорії (складніший момент, залежить від маппера)
                    // Найпростіше: якщо категорія змінилася, маппер має створити нову CategoryEntity з ID
                    if (product.getCategory() != null) {
                        // Тут ми покладаємось на те, що productMapper.toEntity
                        // коректно створює об'єкт категорії всередині.
                        // Але для надійності часто роблять setCategory(categoryRepository.getReferenceById(...))
                        ProductEntity updateSource = productMapper.toEntity(product);
                        existingEntity.setCategory(updateSource.getCategory());
                    }

                    // Зберігаємо зміни
                    return productRepository.save(existingEntity);
                })
                .map(productMapper::toDomain);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}