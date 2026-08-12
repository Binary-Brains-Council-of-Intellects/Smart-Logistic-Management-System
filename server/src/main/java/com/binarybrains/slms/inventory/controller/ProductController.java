package com.binarybrains.slms.inventory.controller;

import com.binarybrains.slms.common.response.ApiResponse;
import com.binarybrains.slms.inventory.dto.*;
import com.binarybrains.slms.inventory.model.ProductCategory;
import com.binarybrains.slms.inventory.model.ProductType;
import com.binarybrains.slms.inventory.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for product/inventory management.
 * This is a thin layer — NO business logic here.
 * All logic is delegated to ProductService.
 */
@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Management", description = "Inventory and product management endpoints")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Uses Factory Method pattern to create PerishableProduct or NonPerishableProduct")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return new ResponseEntity<>(ApiResponse.success("Product created successfully", response),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable String id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getActiveProducts() {
        List<ProductResponse> products = productService.getActiveProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable String id, @Valid @RequestBody UpdateProductRequest request) {
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", response));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate (soft delete) a product")
    public ResponseEntity<ApiResponse<Void>> deactivateProduct(@PathVariable String id) {
        productService.deactivateProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deactivated", null));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Reactivate a product")
    public ResponseEntity<ApiResponse<Void>> activateProduct(@PathVariable String id) {
        productService.activateProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product activated", null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Permanently delete a product")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted", null));
    }

    // --- Search & Filter ---

    @GetMapping("/search")
    @Operation(summary = "Search products by name")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProducts(
            @RequestParam String keyword) {
        List<ProductResponse> results = productService.searchProducts(keyword);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Filter products by category")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getByCategory(
            @PathVariable ProductCategory category) {
        List<ProductResponse> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Filter products by type (PERISHABLE / NON_PERISHABLE)")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getByType(
            @PathVariable ProductType type) {
        List<ProductResponse> products = productService.getProductsByType(type);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/batch/{batchNumber}")
    @Operation(summary = "Get products by batch number")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getByBatch(
            @PathVariable String batchNumber) {
        List<ProductResponse> products = productService.getProductsByBatch(batchNumber);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    // --- Stock Operations ---

    @PatchMapping("/{id}/add-stock")
    @Operation(summary = "Add stock to a product")
    public ResponseEntity<ApiResponse<ProductResponse>> addStock(
            @PathVariable String id, @Valid @RequestBody StockUpdateRequest request) {
        ProductResponse response;
        if (request.getBatchNumber() != null) {
            response = productService.addStock(id, request.getQuantity(), request.getBatchNumber());
        } else {
            response = productService.addStock(id, request.getQuantity());
        }
        return ResponseEntity.ok(ApiResponse.success("Stock added successfully", response));
    }

    @PatchMapping("/{id}/deduct-stock")
    @Operation(summary = "Deduct stock from a product")
    public ResponseEntity<ApiResponse<ProductResponse>> deductStock(
            @PathVariable String id, @Valid @RequestBody StockUpdateRequest request) {
        ProductResponse response = productService.deductStock(id, request.getQuantity());
        return ResponseEntity.ok(ApiResponse.success("Stock deducted successfully", response));
    }

    @GetMapping("/{id}/availability")
    @Operation(summary = "Check product availability")
    public ResponseEntity<ApiResponse<Boolean>> checkAvailability(
            @PathVariable String id, @RequestParam int quantity) {
        boolean available = productService.checkAvailability(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(available));
    }

    // --- Expiry & Stock Alerts ---

    @GetMapping("/expired")
    @Operation(summary = "Get all expired products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getExpiredProducts() {
        List<ProductResponse> products = productService.getExpiredProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/near-expiry")
    @Operation(summary = "Get products near expiry")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getNearExpiryProducts(
            @RequestParam(defaultValue = "30") int days) {
        List<ProductResponse> products = productService.getNearExpiryProducts(days);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get low-stock products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getLowStockProducts(
            @RequestParam(defaultValue = "10") int threshold) {
        List<ProductResponse> products = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(ApiResponse.success(products));
    }
}
