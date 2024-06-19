package com.car_equipment.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "order_product")
@Data
public class OrderProduct implements Serializable {
    @EmbeddedId
    private OrderProductId id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private int quantity;
}