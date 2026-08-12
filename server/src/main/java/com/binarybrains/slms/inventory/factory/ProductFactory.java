package com.binarybrains.slms.inventory.factory;

import com.binarybrains.slms.inventory.dto.CreateProductRequest;
import com.binarybrains.slms.inventory.model.*;
import org.springframework.stereotype.Component;

/**
 * ====================================================================
 * FACTORY METHOD PATTERN: ProductFactory
 * ====================================================================
 * OOP Concept: Factory Method Pattern
 *
 * Creates the appropriate Product subclass based on the ProductType.
 * The caller does not need to know which concrete class is created:
 *
 *   Product product = productFactory.createProduct(request);
 *
 * This decouples object creation from usage and centralizes the
 * instantiation logic in a single place.
 *
 * Spring manages this as a Singleton bean (@Component).
 * ====================================================================
 */
@Component
public class ProductFactory {

    /**
     * Factory method that creates the correct Product subclass.
     *
     * @param request the product creation request
     * @return a PerishableProduct or NonPerishableProduct instance
     * @throws IllegalArgumentException if the product type is unknown
     */
    public Product createProduct(CreateProductRequest request) {
        return switch (request.getProductType()) {
            case PERISHABLE -> createPerishableProduct(request);
            case NON_PERISHABLE -> createNonPerishableProduct(request);
        };
    }

    /**
     * Overloaded factory method — creates a product with explicit type parameter.
     * Demonstrates METHOD OVERLOADING.
     */
    public Product createProduct(ProductType type, String name, String sku, ProductCategory category) {
        return switch (type) {
            case PERISHABLE -> {
                PerishableProduct p = new PerishableProduct();
                p.setName(name);
                p.setSku(sku);
                p.setCategory(category);
                yield p;
            }
            case NON_PERISHABLE -> {
                NonPerishableProduct p = new NonPerishableProduct();
                p.setName(name);
                p.setSku(sku);
                p.setCategory(category);
                yield p;
            }
        };
    }

    // --- Private helper methods ---

    private PerishableProduct createPerishableProduct(CreateProductRequest request) {
        PerishableProduct product = new PerishableProduct(
                request.getName(),
                request.getSku(),
                request.getDescription(),
                request.getCategory(),
                request.getBatchNumber(),
                request.getProductionDate(),
                request.getExpiryDate(),
                request.getQuantity(),
                request.getCostPrice(),
                request.getSellingPrice(),
                request.getStorageTemperatureCelsius() != null ? request.getStorageTemperatureCelsius() : 4.0,
                request.getRequiresRefrigeration() != null ? request.getRequiresRefrigeration() : true
        );
        return product;
    }

    private NonPerishableProduct createNonPerishableProduct(CreateProductRequest request) {
        NonPerishableProduct product = new NonPerishableProduct(
                request.getName(),
                request.getSku(),
                request.getDescription(),
                request.getCategory(),
                request.getBatchNumber(),
                request.getProductionDate(),
                request.getQuantity(),
                request.getCostPrice(),
                request.getSellingPrice(),
                request.getWarrantyMonths() != null ? request.getWarrantyMonths() : 0,
                request.getWeightKg() != null ? request.getWeightKg() : 0.0,
                request.getFragile() != null ? request.getFragile() : false
        );
        return product;
    }
}
