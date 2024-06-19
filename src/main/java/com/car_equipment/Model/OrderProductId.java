package com.car_equipment.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class OrderProductId implements Serializable {
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "product_id")
    private String productId;

    // Constructors, equals, and hashCode methods
}