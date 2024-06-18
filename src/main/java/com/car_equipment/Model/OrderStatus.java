package com.car_equipment.Model;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("Đang chờ"),
    PROCESSING("Đang chuẩn bị hàng"),
    SHIPPED("Đang vận chuyển"),
    DELIVERED("Đã giao"),
    CANCELLED("Đã huỷ");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }

    }