package com.car_equipment.DTO;

import com.car_equipment.Model.Cart;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class CartDTO {
    private String id;
    private String userId;
    private Set<ProductCartDTO> products;

    public static CartDTO transferToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setProducts(cart.getCartProducts().stream().map(cartProduct -> {
            ProductCartDTO productCartDTO = new ProductCartDTO();
            productCartDTO.setProduct(ProductDTO.transferToDTO(cartProduct.getProduct()));
            productCartDTO.setQuantity(cartProduct.getQuantity());
            return productCartDTO;
        }).collect(Collectors.toSet()));
        return dto;
    }
}