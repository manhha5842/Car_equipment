package com.car_equipment.Controller;

import com.car_equipment.DTO.ProductDTO;
import com.car_equipment.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Lấy danh sách Product
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Xem chi tiết Product
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable String id) {
        ProductDTO product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }

    // Thêm Product
    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO newProduct = productService.addProduct(productDTO);
        return ResponseEntity.ok(newProduct);
    }

    // Sửa Product
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable String id, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        }
        return ResponseEntity.notFound().build();
    }

    // Xoá Product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        boolean isDeleted = productService.deleteProduct(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Tìm kiếm sản phẩm theo từ khoá
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String keyword) {
        List<ProductDTO> products = productService.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }

    // Lấy sản phẩm theo category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable String categoryId) {
        List<ProductDTO> products = productService.getProductsByCategory(categoryId);
        if (products != null) {
            return ResponseEntity.ok(products);
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/page")
    public ResponseEntity<Page<ProductDTO>> getProductsByPage(@RequestParam int page, @RequestParam int size) {
        Page<ProductDTO> productPage = productService.getProductsByPage(page, size);
        return ResponseEntity.ok(productPage);
    }
}