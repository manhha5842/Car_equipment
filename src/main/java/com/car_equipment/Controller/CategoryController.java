package com.car_equipment.Controller;

import com.car_equipment.DTO.CategoryDTO;
import com.car_equipment.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Lấy danh sách Category
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Xem chi tiết Category
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable String id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        if (category != null) {
            return ResponseEntity.ok(category);
        }
        return ResponseEntity.notFound().build();
    }

    // Thêm Category
    @PostMapping
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO newCategory = categoryService.addCategory(categoryDTO);
        return ResponseEntity.ok(newCategory);
    }

    // Sửa Category
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable String id, @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
        if (updatedCategory != null) {
            return ResponseEntity.ok(updatedCategory);
        }
        return ResponseEntity.notFound().build();
    }

    // Xoá Category
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        boolean isDeleted = categoryService.deleteCategory(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}