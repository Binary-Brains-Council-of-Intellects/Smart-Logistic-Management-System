package com.binarybrains.slms.inventory.service;

import com.binarybrains.slms.common.exception.InsufficientStockException;
import com.binarybrains.slms.common.exception.ProductNotFoundException;
import com.binarybrains.slms.inventory.dto.CreateProductRequest;
import com.binarybrains.slms.inventory.dto.ProductResponse;
import com.binarybrains.slms.inventory.dto.UpdateProductRequest;
import com.binarybrains.slms.inventory.factory.ProductFactory;
import com.binarybrains.slms.inventory.model.*;
import com.binarybrains.slms.inventory.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Concrete implementation of ProductService.
 *
 * This is a Spring-managed Singleton bean (@Service).
 * Demonstrates ENCAPSULATION of business logic — all stock,
 * expiry, and validation rules live here, not in the controller.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductFactory productFactory;

    /**
     * Constructor injection — Spring injects the singleton instances.
     */
    public ProductServiceImpl(ProductRepository productRepository, ProductFactory productFactory) {
        this.productRepository = productRepository;
        this.productFactory = productFactory;
    }

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        // Validate SKU uniqueness
        if (productRepository.existsBySku(request.getSku())) {
            throw new IllegalArgumentException("A product with SKU '" + request.getSku() + "' already exists");
        }

        // Use Factory Method to create the correct subclass
        Product product = productFactory.createProduct(request);
        Product saved = productRepository.save(product);
        return ProductResponse.fromProduct(saved);
    }

    @Override
    public ProductResponse getProductById(String productId) {
        Product product = findProductOrThrow(productId);
        return ProductResponse.fromProduct(product);
    }

    @Override
    public Product getProductEntityById(String productId) {
        return findProductOrThrow(productId);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponse::fromProduct)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getActiveProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(ProductResponse::fromProduct)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse updateProduct(String productId, UpdateProductRequest request) {
        Product product = findProductOrThrow(productId);

        // Update only non-null fields
        if (request.getName() != null) product.setName(request.getName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getCategory() != null) product.setCategory(request.getCategory());
        if (request.getBatchNumber() != null) product.setBatchNumber(request.getBatchNumber());
        if (request.getProductionDate() != null) product.setProductionDate(request.getProductionDate());
        if (request.getExpiryDate() != null) product.setExpiryDate(request.getExpiryDate());
        if (request.getCostPrice() != null) product.setCostPrice(request.getCostPrice());
        if (request.getSellingPrice() != null) product.setSellingPrice(request.getSellingPrice());

        // Update subclass-specific fields
        if (product instanceof PerishableProduct perishable) {
            if (request.getStorageTemperatureCelsius() != null) {
                perishable.setStorageTemperatureCelsius(request.getStorageTemperatureCelsius());
            }
            if (request.getRequiresRefrigeration() != null) {
                perishable.setRequiresRefrigeration(request.getRequiresRefrigeration());
            }
        }

        if (product instanceof NonPerishableProduct nonPerishable) {
            if (request.getWarrantyMonths() != null) {
                nonPerishable.setWarrantyMonths(request.getWarrantyMonths());
            }
            if (request.getWeightKg() != null) {
                nonPerishable.setWeightKg(request.getWeightKg());
            }
            if (request.getFragile() != null) {
                nonPerishable.setFragile(request.getFragile());
            }
        }

        product.setUpdatedAt(LocalDateTime.now());
        Product saved = productRepository.save(product);
        return ProductResponse.fromProduct(saved);
    }

    @Override
    public void deactivateProduct(String productId) {
        Product product = findProductOrThrow(productId);
        product.deactivate();
        productRepository.save(product);
    }

    @Override
    public void activateProduct(String productId) {
        Product product = findProductOrThrow(productId);
        product.activate();
        productRepository.save(product);
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchByName(keyword).stream()
                .map(ProductResponse::fromProduct)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsByCategory(ProductCategory category) {
        return productRepository.findByCategoryAndActiveTrue(category).stream()
                .map(ProductResponse::fromProduct)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsByType(ProductType productType) {
        return productRepository.findByProductTypeAndActiveTrue(productType).stream()
                .map(ProductResponse::fromProduct)
                .collect(Collectors.toList());
    }

    /**
     * Adds stock through the domain model's controlled method.
     * Demonstrates ENCAPSULATION — the service calls product.addStock(),
     * which enforces business rules internally.
     */
    @Override
    public ProductResponse addStock(String productId, int quantity) {
        Product product = findProductOrThrow(productId);
        product.addStock(quantity); // Domain method enforces rules
        Product saved = productRepository.save(product);
        return ProductResponse.fromProduct(saved);
    }

    /**
     * METHOD OVERLOADING in service layer — adds stock with batch info.
     */
    @Override
    public ProductResponse addStock(String productId, int quantity, String batchNumber) {
        Product product = findProductOrThrow(productId);
        product.addStock(quantity, batchNumber); // Overloaded domain method
        Product saved = productRepository.save(product);
        return ProductResponse.fromProduct(saved);
    }

    /**
     * Deducts stock through the domain model.
     * Throws InsufficientStockException if not enough stock available.
     */
    @Override
    public ProductResponse deductStock(String productId, int quantity) {
        Product product = findProductOrThrow(productId);

        if (product.getAvailableQuantity() < quantity) {
            throw new InsufficientStockException(productId, quantity, product.getAvailableQuantity());
        }

        product.dispatch(quantity); // Domain method enforces rules
        Product saved = productRepository.save(product);
        return ProductResponse.fromProduct(saved);
    }

    @Override
    public boolean checkAvailability(String productId, int quantity) {
        Product product = findProductOrThrow(productId);
        return product.getAvailableQuantity() >= quantity && !product.isExpired();
    }

    @Override
    public List<ProductResponse> getExpiredProducts() {
        return productRepository.findExpiredProducts(LocalDate.now()).stream()
                .map(ProductResponse::fromProduct)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getNearExpiryProducts(int days) {
        LocalDate now = LocalDate.now();
        LocalDate threshold = now.plusDays(days);
        return productRepository.findNearExpiryProducts(now, threshold).stream()
                .map(ProductResponse::fromProduct)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getLowStockProducts(int threshold) {
        return productRepository.findLowStockProducts(threshold).stream()
                .map(ProductResponse::fromProduct)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsByBatch(String batchNumber) {
        return productRepository.findByBatchNumber(batchNumber).stream()
                .map(ProductResponse::fromProduct)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProduct(String productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        productRepository.deleteById(productId);
    }

    // --- Private helper ---

    private Product findProductOrThrow(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
