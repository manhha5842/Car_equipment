package com.car_equipment.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class CartProductId implements Serializable {
    @Column(name = "cart_id")
    private String cartId;

    @Column(name = "product_id")
    private String productId;

    // Constructors, equals, and hashCode methods
}