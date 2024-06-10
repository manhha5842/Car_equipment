package com.car_equipment.DTO;

import com.car_equipment.Model.Category;
import lombok.Data;

@Data
public class CategoryDTO {
    private String id;
    private String name;
    private String imageCategory;

    public static CategoryDTO transferToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setImageCategory(category.getImage_category());
        return dto;
    }
}