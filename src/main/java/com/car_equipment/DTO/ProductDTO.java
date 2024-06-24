package com.car_equipment.DTO;

import com.car_equipment.Model.Product;
import lombok.Data;

import java.sql.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ProductDTO {
    private String id;
    private String name;
    private String description;
    private int price;
    private Double rate;
    private String image;
    private Date date;
    private int quantityInit;
    private int quantityAvailable;
    private int numberOfSale;
    private int viewCount;
    private Set<CategoryDTO> categories;

    public static ProductDTO transferToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setRate(product.getRate());
        dto.setImage(product.getImage());
        dto.setDate(product.getDate());
        dto.setQuantityInit(product.getQuantityInit());
        dto.setQuantityAvailable(product.getQuantityAvailable());
        dto.setNumberOfSale(product.getOrderProducts()==null  ? 0 : product.getOrderProducts().size());
        dto.setViewCount(product.getViewCount());
        dto.setCategories(product.getCategories().stream().map(CategoryDTO::transferToDTO).collect(Collectors.toSet()));
        return dto;
    }
}