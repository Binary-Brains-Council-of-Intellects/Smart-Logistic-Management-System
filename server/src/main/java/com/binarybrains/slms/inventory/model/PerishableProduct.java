package com.binarybrains.slms.inventory.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * ====================================================================
 * CONCRETE CLASS: PerishableProduct
 * ====================================================================
 * OOP Concepts demonstrated:
 *   - INHERITANCE: Extends abstract Product class.
 *   - INTERFACE IMPLEMENTATION: Implements Perishable interface.
 *   - METHOD OVERRIDING: Overrides calculateStorageRequirement().
 *   - POLYMORPHISM: Same method calls produce different behavior
 *     compared to NonPerishableProduct.
 *
 * Represents products that can expire (food, dairy, pharmaceuticals, etc).
 * Adds cold-storage temperature requirements and expiry-checking behavior.
 * ====================================================================
 */
@Document(collection = "products")
public class PerishableProduct extends Product implements Perishable {

    @Field("storage_temperature_celsius")
    private double storageTemperatureCelsius;

    @Field("requires_refrigeration")
    private boolean requiresRefrigeration;

    // ====================================================================
    // CONSTRUCTORS
    // ====================================================================

    public PerishableProduct() {
        super();
    }

    public PerishableProduct(String name, String sku, String description, ProductCategory category,
                             String batchNumber, LocalDate productionDate, LocalDate expiryDate,
                             int totalQuantity, double costPrice, double sellingPrice,
                             double storageTemperatureCelsius, boolean requiresRefrigeration) {
        super(name, sku, description, category, ProductType.PERISHABLE,
                batchNumber, productionDate, expiryDate, totalQuantity, costPrice, sellingPrice);
        this.storageTemperatureCelsius = storageTemperatureCelsius;
        this.requiresRefrigeration = requiresRefrigeration;
    }

    // ====================================================================
    // Perishable INTERFACE IMPLEMENTATION
    // Demonstrates: Interface-based polymorphism
    // ====================================================================

    /**
     * Checks if this product is near its expiry date.
     *
     * @param days the number of days threshold
     * @return true if the product will expire within the given days
     */
    @Override
    public boolean isNearExpiry(int days) {
        if (getExpiryDate() == null) {
            return false;
        }
        LocalDate threshold = LocalDate.now().plusDays(days);
        return !isExpired() && !getExpiryDate().isAfter(threshold);
    }

    /**
     * Returns the expiration date.
     */
    @Override
    public LocalDate getExpirationDate() {
        return getExpiryDate();
    }

    /**
     * Checks if the product has expired.
     */
    @Override
    public boolean checkExpiry() {
        return isExpired();
    }

    // ====================================================================
    // METHOD OVERRIDING — Polymorphism
    // ====================================================================

    /**
     * Perishable products require cold/specialized storage.
     * This override provides a fundamentally different implementation
     * compared to NonPerishableProduct.calculateStorageRequirement().
     */
    @Override
    public String calculateStorageRequirement() {
        if (requiresRefrigeration) {
            return String.format("COLD STORAGE: Maintain at %.1f°C. Refrigeration required. " +
                            "Shelf life remaining: %d days.",
                    storageTemperatureCelsius, getRemainingShelfLifeDays());
        }
        return String.format("COOL STORAGE: Maintain below %.1f°C. Shelf life remaining: %d days.",
                storageTemperatureCelsius, getRemainingShelfLifeDays());
    }

    // ====================================================================
    // PERISHABLE-SPECIFIC DOMAIN METHODS
    // ====================================================================

    /**
     * Calculates the number of days until expiry.
     *
     * @return remaining shelf life in days, or 0 if expired/no expiry date
     */
    public long getRemainingShelfLifeDays() {
        if (getExpiryDate() == null || isExpired()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), getExpiryDate());
    }

    // ====================================================================
    // GETTERS AND SETTERS for perishable-specific fields
    // ====================================================================

    public double getStorageTemperatureCelsius() {
        return storageTemperatureCelsius;
    }

    public void setStorageTemperatureCelsius(double storageTemperatureCelsius) {
        this.storageTemperatureCelsius = storageTemperatureCelsius;
    }

    public boolean isRequiresRefrigeration() {
        return requiresRefrigeration;
    }

    public void setRequiresRefrigeration(boolean requiresRefrigeration) {
        this.requiresRefrigeration = requiresRefrigeration;
    }

    @Override
    public String toString() {
        return String.format("PerishableProduct{id='%s', name='%s', sku='%s', expiry=%s, temp=%.1f°C}",
                getProductId(), getName(), getSku(), getExpiryDate(), storageTemperatureCelsius);
    }
}
