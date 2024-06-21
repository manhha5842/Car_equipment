package com.car_equipment.Model;

import lombok.Getter;

@Getter
public enum EnumAddressStatus {
    ACTIVE("Hoạt động"),
    INACTIVE("Không hoạt động"),
    DELETED("Đã xóa");
    private final String displayName;

    EnumAddressStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}