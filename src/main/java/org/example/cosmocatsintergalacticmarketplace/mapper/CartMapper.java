package org.example.cosmocatsintergalacticmarketplace.mapper;

import org.example.cosmocatsintergalacticmarketplace.domain.Cart;
import org.example.cosmocatsintergalacticmarketplace.dto.CartDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CartMapper {
    CartDTO toCartDTO(Cart cart);
    Cart toCartDomain(CartDTO cartDTO);
}
