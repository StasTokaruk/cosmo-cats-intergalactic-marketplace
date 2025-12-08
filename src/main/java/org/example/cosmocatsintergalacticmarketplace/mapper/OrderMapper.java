package org.example.cosmocatsintergalacticmarketplace.mapper;

import org.example.cosmocatsintergalacticmarketplace.domain.Order;
import org.example.cosmocatsintergalacticmarketplace.dto.OrderDTO;
import org.example.cosmocatsintergalacticmarketplace.repositories.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderMapper {
    OrderDTO toOrderDTO(Order order);
    Order toOrderDomain(OrderDTO orderDTO);

    @Mapping(target = "products", ignore = true)
    Order toDomain(OrderEntity entity);

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    OrderEntity toEntity(Order order);
}