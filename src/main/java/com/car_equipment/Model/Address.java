package com.car_equipment.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Entity
@Table(name = "address")
@Data
public class Address implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(name = "unit_number")
    private int unit_number;
    @Column(name = "street")
    private String street;
    @Column(name = "district")
    private String district;
    @Column(name = "city")
    private String city;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
