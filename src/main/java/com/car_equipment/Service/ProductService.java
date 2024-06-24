package com.car_equipment.Service;

import com.car_equipment.DTO.CategoryDTO;
import com.car_equipment.DTO.ProductBestSellingDTO;
import com.car_equipment.DTO.ProductDTO;
import com.car_equipment.Model.Product;
import com.car_equipment.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Lấy danh sách Product
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductDTO::transferToDTO).collect(Collectors.toList());
    }

    // Lấy danh sách Product theo trang
    public Page<ProductDTO> getProductsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(ProductDTO::transferToDTO);
    }

    // Lấy danh sách sản phẩm theo category
    public List<ProductDTO> getProductsByCategoryId(String categoryId) {
        List<Product> products = productRepository.findProductsByCategoryId(categoryId);
        return products.stream().map(ProductDTO::transferToDTO).collect(Collectors.toList());
    }

    // Lấy danh sách sản phẩm theo category và trang
    public Page<ProductDTO> getProductsByCategoryId(String categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findProductsByCategoryId(categoryId, pageable);
        return productPage.map(ProductDTO::transferToDTO);
    }

    // Lấy danh sách sản phẩm bán chạy
    public List<ProductBestSellingDTO> getBestSellingProducts() {
        List<Product> products = productRepository.findBestSellingProducts();
        return products.stream().map(ProductBestSellingDTO::transferToDTO).collect(Collectors.toList());
    }

    // Lấy danh sách sản phẩm bán chạy theo trang
    public Page<ProductDTO> getBestSellingProductsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findBestSellingProducts(pageable);
        return productPage.map(ProductDTO::transferToDTO);
    }

    // Lấy danh sách sản phẩm được xem nhiều nhất
    public List<ProductDTO> getMostViewedProducts() {
        List<Product> products = productRepository.findMostViewedProducts();
        return products.stream().map(ProductDTO::transferToDTO).collect(Collectors.toList());
    }

    // Lấy danh sách sản phẩm được xem nhiều nhất theo trang
    public Page<ProductDTO> getMostViewedProductsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findMostViewedProducts(pageable);
        return productPage.map(ProductDTO::transferToDTO);
    }

    // Xem chi tiết Product
    public ProductDTO getProductById(String id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setViewCount(product.getViewCount() + 1);
            productRepository.save(product);
            return ProductDTO.transferToDTO(product);
        }
        return null;
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
        product.setCategories(productDTO.getCategories().stream().map(CategoryDTO::transferToEntity).collect(Collectors.toSet()));
        product.setViewCount(0);
        Product savedProduct = productRepository.save(product);
        return ProductDTO.transferToDTO(savedProduct);
    }

    // Sửa Product
    public ProductDTO updateProduct(String id, ProductDTO productDTO) {
        return productRepository.findById(id).map(product -> {
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setRate(productDTO.getRate());
            product.setImage(productDTO.getImage());
            product.setDate(productDTO.getDate());
            product.setQuantityInit(productDTO.getQuantityInit());
            product.setQuantityAvailable(productDTO.getQuantityAvailable());
            product.setCategories(productDTO.getCategories().stream().map(CategoryDTO::transferToEntity).collect(Collectors.toSet()));

            return ProductDTO.transferToDTO(productRepository.save(product));
        }).orElse(null);
    }

    // Xoá Product
    public boolean deleteProduct(String id) {
        return productRepository.findById(id).map(product -> {
            productRepository.delete(product);
            return true;
        }).orElse(false);
    }

    // Tìm kiếm sản phẩm theo từ khoá
    public List<ProductDTO> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword)
                .stream().map(ProductDTO::transferToDTO).collect(Collectors.toList());
    }
}