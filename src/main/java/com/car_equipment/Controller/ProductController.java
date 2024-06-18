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

    // Lấy danh sách Product theo trang
        @GetMapping("/page")
    public ResponseEntity<Page<ProductDTO>> getProductsByPage(@RequestParam int page, @RequestParam int size) {
        Page<ProductDTO> productPage = productService.getProductsByPage(page, size);
        return ResponseEntity.ok(productPage);
    }

    // Lấy danh sách sản phẩm bán chạy
    @GetMapping("/best-selling")
    public ResponseEntity<List<ProductDTO>> getBestSellingProducts() {
        List<ProductDTO> products = productService.getBestSellingProducts();
        return ResponseEntity.ok(products);
    }

    // Lấy danh sách sản phẩm bán chạy theo trang
    @GetMapping("/best-selling/page")
    public ResponseEntity<Page<ProductDTO>> getBestSellingProductsByPage(@RequestParam int page, @RequestParam int size) {
        Page<ProductDTO> productPage = productService.getBestSellingProductsByPage(page, size);
        return ResponseEntity.ok(productPage);
    }

    // Lấy sản phẩm theo category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable String categoryId) {
        List<ProductDTO> products = productService.getProductsByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }

    // Lấy sản phẩm theo category và trang
    @GetMapping("/category/{categoryId}/page")
    public ResponseEntity<Page<ProductDTO>> getProductsByCategory(@PathVariable String categoryId, @RequestParam int page, @RequestParam int size) {
        Page<ProductDTO> productPage = productService.getProductsByCategoryId(categoryId, page, size);
        return ResponseEntity.ok(productPage);
    }
    // Lấy danh sách sản phẩm được xem nhiều nhất
    @GetMapping("/most-viewed")
    public ResponseEntity<List<ProductDTO>> getMostViewedProducts() {
        List<ProductDTO> products = productService.getMostViewedProducts();
        return ResponseEntity.ok(products);
    }

    // Lấy danh sách sản phẩm được xem nhiều nhất theo trang
    @GetMapping("/most-viewed/page")
    public ResponseEntity<Page<ProductDTO>> getMostViewedProductsByPage(@RequestParam int page, @RequestParam int size) {
        Page<ProductDTO> productPage = productService.getMostViewedProductsByPage(page, size);
        return ResponseEntity.ok(productPage);
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
}