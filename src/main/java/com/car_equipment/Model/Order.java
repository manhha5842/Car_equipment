package com.car_equipment.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Time;
import java.util.Set;

@Entity
@Table(name = "`order`")
@Data
public class Order implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_address_id", referencedColumnName = "id")
    private Address address;

    @Column(name = "order_datetime")
    private Time orderDateTime;

    @Column(name = "delivery_fee")
    private int deliveryFee;

    @Column(name = "total_amount")
    private int totalAmount;

    @Column(name = "is_paid")
    private boolean isPaid;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "review")
    private String review;

    @Column(name = "note")
    private String note;

    @ManyToMany
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products;
}