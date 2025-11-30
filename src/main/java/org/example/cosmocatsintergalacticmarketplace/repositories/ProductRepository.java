package org.example.cosmocatsintergalacticmarketplace.repositories;

import org.example.cosmocatsintergalacticmarketplace.repositories.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    boolean existsByNameAndCategory_Id(String name, Long categoryId);
}