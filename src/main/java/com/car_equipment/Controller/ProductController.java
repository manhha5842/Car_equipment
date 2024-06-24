package com.car_equipment.Controller;

import com.car_equipment.DTO.CategoryDTO;
import com.car_equipment.DTO.ProductBestSellingDTO;
import com.car_equipment.DTO.ProductDTO;
import com.car_equipment.Model.Category;
import com.car_equipment.Service.CategoryService;
import com.car_equipment.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Value("${FreeimageHostApiKey}")
    private String freeimageHostApiKey;
    final CategoryService categoryService;

    public ProductController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Lấy danh sách Product
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);

    } // Lấy danh sách Product mới

    @GetMapping("/new")
    public ResponseEntity<List<ProductDTO>> getProductsNew() {
        List<ProductDTO> products = productService.findTop10NewestProducts();
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
    public ResponseEntity<?> getBestSellingProducts() {
        List<ProductBestSellingDTO> products = productService.getBestSellingProducts();
        return ResponseEntity.ok(products);
    }

    // Lấy danh sách sản phẩm bán chạy theo trang
    @GetMapping("/best-selling/page")
    public ResponseEntity<Page<ProductBestSellingDTO>> getBestSellingProductsByPage(@RequestParam int page, @RequestParam int size) {
        Page<ProductBestSellingDTO> productPage = productService.getBestSellingProductsByPage(page, size);
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
//    @PostMapping
//    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) {
//        ProductDTO newProduct = productService.addProduct(productDTO);
//        return ResponseEntity.ok(newProduct);
//    }

    @PostMapping
    public ResponseEntity<?> addProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") int price,
            @RequestParam("quantityInit") int quantityInit,
            @RequestParam("categoryId") String categoryId,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            String imagePath = "https://static.vecteezy.com/system/resources/thumbnails/002/318/271/small_2x/user-profile-icon-free-vector.jpg";
            if (image != null && !image.isEmpty()) {
                imagePath = uploadImageToFreeimageHost(image);
            }
            ProductDTO productDTO = new ProductDTO();
            productDTO.setName(name);
            productDTO.setDescription(description);
            productDTO.setPrice(price);
            productDTO.setQuantityInit(quantityInit);
            productDTO.setRate((double) 0);
            productDTO.setImage(imagePath);
            productDTO.setQuantityAvailable(quantityInit);
            CategoryDTO category = categoryService.getCategoryById(categoryId);
            Set<CategoryDTO> categorySet = new HashSet<>();
            categorySet.add(category);
            productDTO.setCategories(categorySet);

            ProductDTO newProduct = productService.addProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        } catch (IllegalStateException | IOException e) {
            System.out.println(e);
            return ResponseEntity.badRequest().body(e);
        }
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

    private String uploadImageToFreeimageHost(MultipartFile image) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("key", freeimageHostApiKey);
        body.add("action", "upload");
        body.add("source", new ByteArrayResource(image.getBytes()) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://freeimage.host/api/1/upload",
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("image")) {
                Map<String, Object> imageInfo = (Map<String, Object>) responseBody.get("image");
                return (String) imageInfo.get("url");
            }
        }

        throw new IOException("Failed to upload image to Freeimage.host");
    }
}