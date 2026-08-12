package com.binarybrains.slms.inventory.service;

import com.binarybrains.slms.inventory.dto.CreateProductRequest;
import com.binarybrains.slms.inventory.dto.ProductResponse;
import com.binarybrains.slms.inventory.dto.UpdateProductRequest;
import com.binarybrains.slms.inventory.model.Product;
import com.binarybrains.slms.inventory.model.ProductCategory;
import com.binarybrains.slms.inventory.model.ProductType;

import java.util.List;

/**
 * Service interface for product operations.
 * Demonstrates ABSTRACTION — the controller depends on this interface,
 * not on the concrete implementation.
 */
public interface ProductService {

    ProductResponse createProduct(CreateProductRequest request);

    ProductResponse getProductById(String productId);

    Product getProductEntityById(String productId);

    List<ProductResponse> getAllProducts();

    List<ProductResponse> getActiveProducts();

    ProductResponse updateProduct(String productId, UpdateProductRequest request);

    void deactivateProduct(String productId);

    void activateProduct(String productId);

    List<ProductResponse> searchProducts(String keyword);

    List<ProductResponse> getProductsByCategory(ProductCategory category);

    List<ProductResponse> getProductsByType(ProductType productType);

    ProductResponse addStock(String productId, int quantity);

    ProductResponse addStock(String productId, int quantity, String batchNumber);

    ProductResponse deductStock(String productId, int quantity);

    boolean checkAvailability(String productId, int quantity);

    List<ProductResponse> getExpiredProducts();

    List<ProductResponse> getNearExpiryProducts(int days);

    List<ProductResponse> getLowStockProducts(int threshold);

    List<ProductResponse> getProductsByBatch(String batchNumber);

    void deleteProduct(String productId);
}
