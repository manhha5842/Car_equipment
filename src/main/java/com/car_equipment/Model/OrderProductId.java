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

    // Constructor mặc định (không tham số)
    public OrderProductId() {
    }

    // Constructor với tham số
    public OrderProductId(String orderId, String productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    // equals và hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderProductId that = (OrderProductId) o;

        if (!orderId.equals(that.orderId)) return false;
        return productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        int result = orderId.hashCode();
        result = 31 * result + productId.hashCode();
        return result;
    }
}