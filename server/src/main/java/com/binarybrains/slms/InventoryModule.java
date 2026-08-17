package com.binarybrains.slms;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Inventory & Product Management Module
 */
public class InventoryModule {

    // Abstraction
    public static abstract class AbstractProduct {
        // Encapsulation
        private String id;
        private String name;

        public AbstractProduct() {}

        // Polymorphism
        public AbstractProduct(String id, String name) {
            this.id = id;
            this.name = name;
        }

        // Encapsulation
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        // Abstraction
        public abstract double getDiscountedPrice(double discountPercentage);

        // Abstraction
        public abstract String getProductDetails();
    }

    public enum Category {
        ELECTRONICS, GROCERY, PHARMACEUTICAL, CLOTHING, OTHER
    }

    public enum ProductType {
        PerishableProduct, NonPerishableProduct
    }

    // Inheritance
    public static class Product extends AbstractProduct {

        // Encapsulation
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

        // Polymorphism
        public Product() {
            super();
        }

        // Polymorphism
        public Product(String id, String name, Category category, ProductType type, String batchNumber,
                       String productionDate, String expiryDate, int totalQuantity, int availableQuantity,
                       int expiredQuantity, double costPrice, double sellingPrice) {
            super(id, name);
            this.category = category;
            this.type = type;
            this.batchNumber = batchNumber;
            this.productionDate = productionDate;
            this.expiryDate = expiryDate;
            setTotalQuantity(totalQuantity);
            setAvailableQuantity(availableQuantity);
            setExpiredQuantity(expiredQuantity);
            setCostPrice(costPrice);
            setSellingPrice(sellingPrice);
        }

        // Polymorphism
        public Product(String id, String name, Category category, double costPrice, double sellingPrice) {
            this(id, name, category, ProductType.NonPerishableProduct, "BATCH-001",
                 LocalDate.now().toString(), "N/A", 100, 100, 0, costPrice, sellingPrice);
        }

        // Polymorphism
        @Override
        public double getDiscountedPrice(double discountPercentage) {
            double validDiscount = Math.min(100.0, Math.max(0.0, discountPercentage));
            return this.sellingPrice * (1.0 - (validDiscount / 100.0));
        }

        // Polymorphism
        @Override
        public String getProductDetails() {
            return "Product [" + getId() + "] " + getName() + " (Category: " + category + ") - Stock: " + availableQuantity;
        }

        // Polymorphism
        public double calculateProfitMargin() {
            if (this.costPrice <= 0) return 0.0;
            return ((this.sellingPrice - this.costPrice) / this.costPrice) * 100.0;
        }

        // Polymorphism
        public double calculateProfitMargin(double promotionalDiscountPercent) {
            double discountedSellingPrice = getDiscountedPrice(promotionalDiscountPercent);
            if (this.costPrice <= 0) return 0.0;
            return ((discountedSellingPrice - this.costPrice) / this.costPrice) * 100.0;
        }

        // Encapsulation
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
        public void setTotalQuantity(int totalQuantity) {
            this.totalQuantity = Math.max(0, totalQuantity);
        }

        public int getAvailableQuantity() { return availableQuantity; }
        public void setAvailableQuantity(int availableQuantity) {
            this.availableQuantity = Math.max(0, availableQuantity);
        }

        public int getExpiredQuantity() { return expiredQuantity; }
        public void setExpiredQuantity(int expiredQuantity) {
            this.expiredQuantity = Math.max(0, expiredQuantity);
        }

        public double getCostPrice() { return costPrice; }
        public void setCostPrice(double costPrice) {
            this.costPrice = Math.max(0.0, costPrice);
        }

        public double getSellingPrice() { return sellingPrice; }
        public void setSellingPrice(double sellingPrice) {
            this.sellingPrice = Math.max(0.0, sellingPrice);
        }
    }

    // Abstraction
    public interface ProductRepository {
        List<Product> findAll();
        Optional<Product> findById(String id);
        Product save(Product product);
        boolean deleteById(String id);
        boolean existsById(String id);
        long count();
    }

    // Inheritance
    public static class InMemoryProductRepository implements ProductRepository {
        private final Map<String, Product> store = new ConcurrentHashMap<>();

        public InMemoryProductRepository() {
            save(new Product("PRD-1001", "Wireless Ergonomic Mouse", Category.ELECTRONICS, ProductType.NonPerishableProduct, "BT-99", "2026-01-10", "N/A", 500, 480, 0, 15.0, 35.0));
            save(new Product("PRD-1002", "High-Speed USB-C Cable 2m", Category.ELECTRONICS, ProductType.NonPerishableProduct, "BT-100", "2026-02-01", "N/A", 1000, 950, 0, 4.0, 12.0));
            save(new Product("PRD-1003", "Whole Milk Powder (1kg)", Category.GROCERY, ProductType.PerishableProduct, "BT-101", "2026-03-01", "2027-03-01", 300, 280, 5, 6.0, 14.0));
        }

        @Override public List<Product> findAll() { return new ArrayList<>(store.values()); }
        @Override public Optional<Product> findById(String id) { return Optional.ofNullable(store.get(id)); }
        @Override public Product save(Product product) { store.put(product.getId(), product); return product; }
        @Override public boolean deleteById(String id) { return store.remove(id) != null; }
        @Override public boolean existsById(String id) { return store.containsKey(id); }
        @Override public long count() { return store.size(); }
    }

    // Composition
    public static class ProductHttpHandler implements HttpHandler {
        private final ProductRepository productRepository;

        public ProductHttpHandler(ProductRepository productRepository) {
            this.productRepository = productRepository;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");

            String method = exchange.getRequestMethod();
            if ("OPTIONS".equalsIgnoreCase(method)) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            String path = exchange.getRequestURI().getPath();
            String[] parts = path.split("/");
            String id = (parts.length > 3) ? parts[3] : null;

            try {
                if ("GET".equalsIgnoreCase(method)) {
                    if (id != null && !id.isEmpty()) {
                        Optional<Product> prod = productRepository.findById(id);
                        if (prod.isPresent()) {
                            sendJsonResponse(exchange, 200, SlmsApplication.toJson(prod.get()));
                        } else {
                            sendJsonResponse(exchange, 404, "{\"error\":\"Product not found\"}");
                        }
                    } else {
                        sendJsonResponse(exchange, 200, SlmsApplication.toJson(productRepository.findAll()));
                    }
                } else if ("POST".equalsIgnoreCase(method)) {
                    String body = readRequestBody(exchange);
                    Map<String, String> map = SlmsApplication.parseJsonMap(body);

                    Product prod = new Product();
                    prod.setId(map.containsKey("id") && !map.get("id").isEmpty() ? map.get("id") : "PRD-" + (int)(1000 + Math.random() * 9000));
                    prod.setName(map.getOrDefault("name", "New Product"));
                    try { prod.setCategory(Category.valueOf(map.getOrDefault("category", "ELECTRONICS"))); } catch (Exception e) { prod.setCategory(Category.ELECTRONICS); }
                    try { prod.setType(ProductType.valueOf(map.getOrDefault("type", "NonPerishableProduct"))); } catch (Exception e) { prod.setType(ProductType.NonPerishableProduct); }
                    prod.setBatchNumber(map.getOrDefault("batchNumber", "BATCH-01"));
                    prod.setProductionDate(map.getOrDefault("productionDate", LocalDate.now().toString()));
                    prod.setExpiryDate(map.getOrDefault("expiryDate", "N/A"));
                    int tot = map.containsKey("totalQuantity") ? Integer.parseInt(map.get("totalQuantity")) :
                             (map.containsKey("quantity") ? Integer.parseInt(map.get("quantity")) : 100);
                    int avail = map.containsKey("availableQuantity") ? Integer.parseInt(map.get("availableQuantity")) : tot;

                    prod.setTotalQuantity(tot);
                    prod.setAvailableQuantity(avail);
                    prod.setExpiredQuantity(map.containsKey("expiredQuantity") ? Integer.parseInt(map.get("expiredQuantity")) : 0);
                    prod.setCostPrice(map.containsKey("costPrice") ? Double.parseDouble(map.get("costPrice")) : 10.0);
                    prod.setSellingPrice(map.containsKey("sellingPrice") ? Double.parseDouble(map.get("sellingPrice")) : 20.0);

                    Product saved = productRepository.save(prod);
                    sendJsonResponse(exchange, 200, SlmsApplication.toJson(saved));

                } else if ("PUT".equalsIgnoreCase(method)) {
                    if (id == null || !productRepository.existsById(id)) {
                        sendJsonResponse(exchange, 404, "{\"error\":\"Product not found\"}");
                        return;
                    }
                    String body = readRequestBody(exchange);
                    Map<String, String> map = SlmsApplication.parseJsonMap(body);
                    Optional<Product> existingOpt = productRepository.findById(id);
                    if (existingOpt.isPresent()) {
                        Product prod = existingOpt.get();
                        if (map.containsKey("name")) prod.setName(map.get("name"));
                        if (map.containsKey("costPrice")) prod.setCostPrice(Double.parseDouble(map.get("costPrice")));
                        if (map.containsKey("sellingPrice")) prod.setSellingPrice(Double.parseDouble(map.get("sellingPrice")));
                        if (map.containsKey("availableQuantity")) prod.setAvailableQuantity(Integer.parseInt(map.get("availableQuantity")));
                        productRepository.save(prod);
                        sendJsonResponse(exchange, 200, SlmsApplication.toJson(prod));
                    }
                } else if ("PATCH".equalsIgnoreCase(method)) {
                    if (id != null) {
                        Optional<Product> existingOpt = productRepository.findById(id);
                        if (existingOpt.isPresent()) {
                            Product prod = existingOpt.get();
                            String body = readRequestBody(exchange);
                            Map<String, String> map = SlmsApplication.parseJsonMap(body);
                            int qty = map.containsKey("quantity") ? Integer.parseInt(map.get("quantity")) : 1;

                            if (path.contains("/deduct-stock")) {
                                prod.setAvailableQuantity(Math.max(0, prod.getAvailableQuantity() - qty));
                                prod.setTotalQuantity(Math.max(0, prod.getTotalQuantity() - qty));
                            } else if (path.contains("/add-stock")) {
                                prod.setAvailableQuantity(prod.getAvailableQuantity() + qty);
                                prod.setTotalQuantity(prod.getTotalQuantity() + qty);
                            }
                            productRepository.save(prod);
                            sendJsonResponse(exchange, 200, SlmsApplication.toJson(prod));
                            return;
                        }
                    }
                    sendJsonResponse(exchange, 404, "{\"error\":\"Product not found\"}");
                } else if ("DELETE".equalsIgnoreCase(method)) {
                    if (id != null && productRepository.deleteById(id)) {
                        exchange.sendResponseHeaders(204, -1);
                    } else {
                        sendJsonResponse(exchange, 404, "{\"error\":\"Product not found\"}");
                    }
                } else {
                    exchange.sendResponseHeaders(455, -1);
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"error\":\"" + e.getMessage() + "\"}");
            }
        }

        private String readRequestBody(HttpExchange exchange) throws IOException {
            InputStream is = exchange.getRequestBody();
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }

        private void sendJsonResponse(HttpExchange exchange, int statusCode, String responseJson) throws IOException {
            byte[] bytes = responseJson.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }
}
