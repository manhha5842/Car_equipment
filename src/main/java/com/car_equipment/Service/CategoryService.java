package com.car_equipment.Service;

import com.car_equipment.DTO.CategoryDTO;
import com.car_equipment.Model.Category;
import com.car_equipment.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Lấy danh sách Category
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryDTO::transferToDTO).collect(Collectors.toList());
    }

    // Xem chi tiết Category
    public CategoryDTO getCategoryById(String id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(CategoryDTO::transferToDTO).orElse(null);
    }

    // Thêm Category
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setImageCategory(categoryDTO.getImageCategory());
        Category savedCategory = categoryRepository.save(category);
        return CategoryDTO.transferToDTO(savedCategory);
    }

    // Sửa Category
    public CategoryDTO updateCategory(String id, CategoryDTO categoryDTO) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setName(categoryDTO.getName());
            category.setImageCategory(categoryDTO.getImageCategory());
            Category updatedCategory = categoryRepository.save(category);
            return CategoryDTO.transferToDTO(updatedCategory);
        }
        return null;
    }

    // Xoá Category
    public boolean deleteCategory(String id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}