package org.example.cosmocatsintergalacticmarketplace.repositories;

import org.example.cosmocatsintergalacticmarketplace.repositories.entity.OrderEntity;
import org.example.cosmocatsintergalacticmarketplace.repositories.projection.TopProductProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    @Query("SELECT item.product.name AS productName, SUM(item.quantity) AS totalSold " +
            "FROM OrderEntity o " +
            "JOIN o.items item " +
            "GROUP BY item.product.name " +
            "ORDER BY SUM(item.quantity) DESC")
    List<TopProductProjection> findTopSellingProducts();
}