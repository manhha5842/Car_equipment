package com.car_equipment.Service;

import com.car_equipment.DTO.CategoryDTO;
import com.car_equipment.Model.Category;
import com.car_equipment.Model.Product;
import com.car_equipment.Repository.CategoryRepository;
import com.car_equipment.Repository.ProductRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CSVService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public void importCSVData(String csvFilePath) {
        try {
            ClassPathResource resource = new ClassPathResource(csvFilePath);
            try (CSVReader csvReader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
                List<String[]> records = csvReader.readAll();
                boolean isFirstRow = true;
                for (String[] record : records) {
                    if (isFirstRow) {
                        isFirstRow = false; // Bỏ qua dòng tiêu đề
                        continue;
                    }

                    try {
                        String productName = record[1];
                        String description = record[2];
                        int price = parseIntSafe(record[3]);
                        String imageSource = record[5];
                        String categoryId = record[10];

                        Product product = new Product();
                        product.setName(productName);
                        product.setDescription(description);
                        product.setPrice(price);
                        product.setRate(5.0);
                        product.setImage(imageSource);
                        product.setDate(null);
                        product.setQuantityInit(200);
                        product.setQuantityAvailable(200);

                        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
                        if (categoryOptional.isPresent()) {
                            System.out.println(CategoryDTO.transferToDTO(categoryOptional.get()));
                            Set<Category> categoryList = new HashSet<>();
                            categoryList.add(categoryOptional.get());
                            product.setCategories(categoryList);
                        } else {
                            System.out.println(categoryId);
                        }
                        productRepository.save(product);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        // Xử lý các giá trị không hợp lệ hoặc thiếu dữ liệu
                        System.err.println("Lỗi khi xử lý dòng: " + e.getMessage());
                    }
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    private int parseIntSafe(String value) {
        try {
            return value.isEmpty() ? 0 : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.err.println("Lỗi khi chuyển đổi giá trị sang số nguyên: " + value);
            return 0;
        }
    }

    private Double parseDoubleSafe(String value) {
        try {
            return value.isEmpty() ? null : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.err.println("Lỗi khi chuyển đổi giá trị sang số thực: " + value);
            return null;
        }
    }

    private Time parseTimeSafe(String value) {
        try {
            return value.isEmpty() ? null : Time.valueOf(value);
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi khi chuyển đổi giá trị sang thời gian: " + value);
            return null;
        }
    }
}