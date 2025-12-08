package org.example.cosmocatsintergalacticmarketplace.mapper;

import org.example.cosmocatsintergalacticmarketplace.domain.Product;
import org.example.cosmocatsintergalacticmarketplace.dto.ProductAvailabilityDTO;
import org.example.cosmocatsintergalacticmarketplace.dto.ProductDTO;
import org.example.cosmocatsintergalacticmarketplace.dto.ProductDetailDTO;
import org.example.cosmocatsintergalacticmarketplace.repositories.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {

    @Mapping(target = "price", ignore = true)
    @Mapping(target = "quantity", ignore = true)
    @Mapping(target = "description", ignore = true)
    Product toDetailDomain(ProductDetailDTO productDetailDTO);

    @Mapping(target = "price", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "description", ignore = true)
    Product toAvailabilityDomain(ProductAvailabilityDTO productAvailabilityDTO);

    Product toProductDomain(ProductDTO productDTO);

    ProductDetailDTO toDetailDTO(Product product);
    ProductAvailabilityDTO toAvailabilityDTO(Product product);
    ProductDTO toProductDTO(Product product);

    Product toDomain(ProductEntity entity);
    ProductEntity toEntity(Product product);
}