package org.example.cosmocatsintergalacticmarketplace.mapper;

import org.example.cosmocatsintergalacticmarketplace.domain.Product;
import org.example.cosmocatsintergalacticmarketplace.dto.ProductAvailabilityDTO;
import org.example.cosmocatsintergalacticmarketplace.dto.ProductDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    // Мапінг для деталей товару
    ProductDetailDTO toDetailDTO(Product product);
    Product toDomain(ProductDetailDTO productDetailDTO);

    // Мапінг для наявності товару
    ProductAvailabilityDTO toAvailabilityDTO(Product product);
    Product toDomain(ProductAvailabilityDTO productAvailabilityDTO);
}
