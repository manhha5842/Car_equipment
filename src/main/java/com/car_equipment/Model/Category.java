package com.car_equipment.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "category")
@Data
public class Category implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "image_category")
    private String imageCategory;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.EAGER)
    private Set<Product> products;
}