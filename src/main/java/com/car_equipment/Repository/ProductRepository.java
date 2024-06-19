package com.car_equipment.Repository;

import com.car_equipment.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByNameContainingIgnoreCase(String keyword);

    @Query("SELECT p FROM Product p JOIN p.orderProducts op GROUP BY p.id ORDER BY SUM(op.quantity) DESC")
    List<Product> findBestSellingProducts();

    @Query("SELECT p FROM Product p JOIN p.orderProducts op GROUP BY p.id ORDER BY SUM(op.quantity) DESC")
    Page<Product> findBestSellingProducts(Pageable pageable);
    

    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id = :categoryId")
    List<Product> findProductsByCategoryId(String categoryId);

    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id = :categoryId")
    Page<Product> findProductsByCategoryId(String categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p ORDER BY p.viewCount DESC")
    List<Product> findMostViewedProducts();

    @Query("SELECT p FROM Product p ORDER BY p.viewCount DESC")
    Page<Product> findMostViewedProducts(Pageable pageable);
}