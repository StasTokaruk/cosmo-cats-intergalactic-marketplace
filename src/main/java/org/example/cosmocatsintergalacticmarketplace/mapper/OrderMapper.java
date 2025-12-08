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

    @Mapping(target = "products", ignore = true) // Тут потрібна складна логіка мапінгу списків, MapStruct може не впоратись автоматично без конфігу
    Order toDomain(OrderEntity entity);

    @Mapping(target = "items", ignore = true)
    OrderEntity toEntity(Order order);
}
