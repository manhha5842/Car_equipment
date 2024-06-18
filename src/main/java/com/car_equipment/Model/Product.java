package com.car_equipment.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Time;
import java.util.Set;

@Entity
@Table(name = "product")
@Data
public class Product implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "product_name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private int price;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "image_source")
    private String image;

    @Column(name = "date")
    private Time date;

    @Column(name = "quantity_init")
    private int quantityInit;

    @Column(name = "quantity_available")
    private int quantityAvailable;

    @ManyToMany
    @JoinTable(
            name = "category_product",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    @OneToMany(mappedBy = "product")
    private Set<Review> reviews;

    @ManyToMany(mappedBy = "products")
    private Set<Cart> carts;

    @ManyToMany(mappedBy = "products")
    private Set<Order> orders;
}