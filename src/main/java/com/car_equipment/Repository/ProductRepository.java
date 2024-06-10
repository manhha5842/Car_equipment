package com.car_equipment.Repository;

import com.car_equipment.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByNameContainingIgnoreCase(String keyword);
    List<Product> findByCategories_Id(String categoryId);
    Page<Product> findAll(Pageable pageable);
}