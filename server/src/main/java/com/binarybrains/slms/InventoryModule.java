package com.binarybrains.slms;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * MODULE 1: INVENTORY & PRODUCT MANAGEMENT
 * Contains: Product Model, Categories, Repository, and REST Controller (/api/products).
 */
public class InventoryModule {

    // ------------------------------------------------------------------------
    // 1. ENUMS & DATA MODELS
    // ------------------------------------------------------------------------

    public enum Category {
        ELECTRONICS, GROCERY, PHARMACEUTICAL, CLOTHING, OTHER
    }

    public enum ProductType {
        PerishableProduct, NonPerishableProduct
    }

    @Document(collection = "products")
    public static class Product {
        @Id
        private String id;
        private String name;
        private Category category;
        private ProductType type;
        private String batchNumber;
        private String productionDate;
        private String expiryDate;
        private int totalQuantity;
        private int availableQuantity;
        private int expiredQuantity;
        private double costPrice;
        private double sellingPrice;

        public Product() {}

        public Product(String id, String name, Category category, ProductType type, String batchNumber,
                       String productionDate, String expiryDate, int totalQuantity, int availableQuantity,
                       int expiredQuantity, double costPrice, double sellingPrice) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.type = type;
            this.batchNumber = batchNumber;
            this.productionDate = productionDate;
            this.expiryDate = expiryDate;
            this.totalQuantity = totalQuantity;
            this.availableQuantity = availableQuantity;
            this.expiredQuantity = expiredQuantity;
            this.costPrice = costPrice;
            this.sellingPrice = sellingPrice;
        }

        // Domain calculation method: Profit Margin % = ((Selling Price - Cost Price) / Cost Price) * 100
        public double calculateProfitMargin() {
            if (this.costPrice <= 0) return 0.0;
            return ((this.sellingPrice - this.costPrice) / this.costPrice) * 100.0;
        }

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Category getCategory() { return category; }
        public void setCategory(Category category) { this.category = category; }

        public ProductType getType() { return type; }
        public void setType(ProductType type) { this.type = type; }

        public String getBatchNumber() { return batchNumber; }
        public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

        public String getProductionDate() { return productionDate; }
        public void setProductionDate(String productionDate) { this.productionDate = productionDate; }

        public String getExpiryDate() { return expiryDate; }
        public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

        public int getTotalQuantity() { return totalQuantity; }
        public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }

        public int getAvailableQuantity() { return availableQuantity; }
        public void setAvailableQuantity(int availableQuantity) { this.availableQuantity = availableQuantity; }

        public int getExpiredQuantity() { return expiredQuantity; }
        public void setExpiredQuantity(int expiredQuantity) { this.expiredQuantity = expiredQuantity; }

        public double getCostPrice() { return costPrice; }
        public void setCostPrice(double costPrice) { this.costPrice = costPrice; }

        public double getSellingPrice() { return sellingPrice; }
        public void setSellingPrice(double sellingPrice) { this.sellingPrice = sellingPrice; }
    }

    // ------------------------------------------------------------------------
    // 2. MONGO REPOSITORY
    // ------------------------------------------------------------------------

    public interface ProductRepository extends MongoRepository<Product, String> {
        List<Product> findByCategory(Category category);
        List<Product> findByAvailableQuantityLessThanEqual(int quantityThreshold);
    }

    // ------------------------------------------------------------------------
    // 3. REST CONTROLLER
    // ------------------------------------------------------------------------

    @RestController
    @RequestMapping("/api/products")
    @CrossOrigin(origins = "*")
    public static class ProductController {

        private final ProductRepository productRepository;

        public ProductController(ProductRepository productRepository) {
            this.productRepository = productRepository;
        }

        @GetMapping
        public List<Product> getAllProducts() {
            return productRepository.findAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity<Product> getProductById(@PathVariable String id) {
            Optional<Product> product = productRepository.findById(id);
            return product.map(ResponseEntity::ok)
                          .orElseGet(() -> ResponseEntity.notFound().build());
        }

        @PostMapping
        public ResponseEntity<Product> createProduct(@RequestBody Product product) {
            if (product.getId() == null || product.getId().isEmpty()) {
                product.setId("PRD-" + (int)(1000 + Math.random() * 9000));
            }
            Product savedProduct = productRepository.save(product);
            return ResponseEntity.ok(savedProduct);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Product updatedProduct) {
            if (!productRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            updatedProduct.setId(id);
            Product saved = productRepository.save(updatedProduct);
            return ResponseEntity.ok(saved);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
            if (!productRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
}
