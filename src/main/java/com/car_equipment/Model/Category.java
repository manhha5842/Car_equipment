package com.car_equipment.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.List;
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
    private String image_category;

    @ManyToMany(mappedBy = "categories")
    @JsonBackReference
    private Set<Product> products;

}
