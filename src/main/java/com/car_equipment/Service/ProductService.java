package com.car_equipment.Service;

import com.car_equipment.DTO.ProductDTO;
import com.car_equipment.Model.Category;
import com.car_equipment.Model.Product;
import com.car_equipment.Repository.CategoryRepository;
import com.car_equipment.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Lấy danh sách Product
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductDTO::transferToDTO).collect(Collectors.toList());
    }

    // Xem chi tiết Product
    public ProductDTO getProductById(String id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(ProductDTO::transferToDTO).orElse(null);
    }

    // Thêm Product
    public ProductDTO addProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setRate(productDTO.getRate());
        product.setImage(productDTO.getImage());
        product.setDate(productDTO.getDate());
        product.setQuantityInit(productDTO.getQuantityInit());
        product.setQuantityAvailable(productDTO.getQuantityAvailable());

        Set<Category> categories = productDTO.getCategories().stream()
                .map(categoryDTO -> categoryRepository.findById(categoryDTO.getId()).orElse(null))
                .collect(Collectors.toSet());
        product.setCategories(categories);

        Product savedProduct = productRepository.save(product);
        return ProductDTO.transferToDTO(savedProduct);
    }

    // Sửa Product
    public ProductDTO updateProduct(String id, ProductDTO productDTO) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setRate(productDTO.getRate());
            product.setImage(productDTO.getImage());
            product.setDate(productDTO.getDate());
            product.setQuantityInit(productDTO.getQuantityInit());
            product.setQuantityAvailable(productDTO.getQuantityAvailable());

            Set<Category> categories = productDTO.getCategories().stream()
                    .map(categoryDTO -> categoryRepository.findById(categoryDTO.getId()).orElse(null))
                    .collect(Collectors.toSet());
            product.setCategories(categories);

            Product updatedProduct = productRepository.save(product);
            return ProductDTO.transferToDTO(updatedProduct);
        }
        return null;
    }

    // Xoá Product
    public boolean deleteProduct(String id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Tìm kiếm sản phẩm theo từ khoá
    public List<ProductDTO> searchProducts(String keyword) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        return products.stream().map(ProductDTO::transferToDTO).collect(Collectors.toList());
    }

    // Lấy sản phẩm theo category
    public List<ProductDTO> getProductsByCategory(String categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            Set<Product> products = category.getProducts();
            return products.stream().map(ProductDTO::transferToDTO).collect(Collectors.toList());
        }
        return null;
    }

    public Page<ProductDTO> getProductsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(ProductDTO::transferToDTO);
    }

}