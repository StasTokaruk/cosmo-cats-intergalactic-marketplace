package org.example.cosmocatsintergalacticmarketplace.mapper;

import org.example.cosmocatsintergalacticmarketplace.domain.Product;
import org.example.cosmocatsintergalacticmarketplace.dto.ProductAvailabilityDTO;
import org.example.cosmocatsintergalacticmarketplace.dto.ProductDTO;
import org.example.cosmocatsintergalacticmarketplace.dto.ProductDetailDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {
    // Мапінг для деталей товару
    ProductDetailDTO toDetailDTO(Product product);
    Product toDetailDomain(ProductDetailDTO productDetailDTO);

    // Мапінг для наявності товару
    ProductAvailabilityDTO toAvailabilityDTO(Product product);
    Product toAvailabilityDomain(ProductAvailabilityDTO productAvailabilityDTO);

    // Мапінг для всього продукту
    ProductDTO toProductDTO(Product product);
    Product toProductDomain(ProductDTO productDTO);
}
