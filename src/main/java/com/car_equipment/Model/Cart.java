package com.car_equipment.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cart")
@Data
public class Cart implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartProduct> cartProducts = new HashSet<>();

    @PrePersist
    @PreUpdate
    private void ensureCartProductsNotNull() {
        if (cartProducts == null) {
            cartProducts = new HashSet<>();
        }
    }
}