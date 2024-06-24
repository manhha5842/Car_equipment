package com.car_equipment.Controller;

import com.car_equipment.DTO.CategoryDTO;
import com.car_equipment.Service.CategoryService;
import com.car_equipment.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Value("${FreeimageHostApiKey}")
    private String freeimageHostApiKey;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

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


    @PostMapping
    public ResponseEntity<?> updateInfo(
            @RequestParam("name") String name,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            String imagePath = null;
            if (image != null && !image.isEmpty()) {
                imagePath = uploadImageToFreeimageHost(image);
            }
            CategoryDTO newCategory = categoryService.addCategory(name, imagePath);
            return ResponseEntity.ok().body(newCategory);
        } catch (IllegalStateException | IOException e) {
            System.out.println(e);
            return ResponseEntity.badRequest().body(e);
        }
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