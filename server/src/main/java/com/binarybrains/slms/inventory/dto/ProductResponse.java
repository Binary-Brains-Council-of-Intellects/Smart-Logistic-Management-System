package com.binarybrains.slms.inventory.dto;

import com.binarybrains.slms.inventory.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for product API responses.
 * Prevents internal domain model details from leaking to the client.
 */
public class ProductResponse {

    private String productId;
    private String name;
    private String sku;
    private String description;
    private ProductCategory category;
    private ProductType productType;
    private String batchNumber;
    private LocalDate productionDate;
    private LocalDate expiryDate;
    private int totalQuantity;
    private int availableQuantity;
    private int expiredQuantity;
    private double costPrice;
    private double sellingPrice;
    private double profitMargin;
    private String storageRequirement;
    private boolean expired;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Perishable-specific response fields
    private Double storageTemperatureCelsius;
    private Boolean requiresRefrigeration;
    private Boolean nearExpiry;
    private Long remainingShelfLifeDays;

    // Non-perishable-specific response fields
    private Integer warrantyMonths;
    private Double weightKg;
    private Boolean fragile;

    // --- Static factory method to convert domain model to response DTO ---

    public static ProductResponse fromProduct(Product product) {
        ProductResponse response = new ProductResponse();
        response.productId = product.getProductId();
        response.name = product.getName();
        response.sku = product.getSku();
        response.description = product.getDescription();
        response.category = product.getCategory();
        response.productType = product.getProductType();
        response.batchNumber = product.getBatchNumber();
        response.productionDate = product.getProductionDate();
        response.expiryDate = product.getExpiryDate();
        response.totalQuantity = product.getTotalQuantity();
        response.availableQuantity = product.getAvailableQuantity();
        response.expiredQuantity = product.getExpiredQuantity();
        response.costPrice = product.getCostPrice();
        response.sellingPrice = product.getSellingPrice();
        response.profitMargin = product.calculateProfitMargin();
        response.storageRequirement = product.calculateStorageRequirement();
        response.expired = product.isExpired();
        response.active = product.isActive();
        response.createdAt = product.getCreatedAt();
        response.updatedAt = product.getUpdatedAt();

        // Populate perishable-specific fields using interface check
        if (product instanceof PerishableProduct perishable) {
            response.storageTemperatureCelsius = perishable.getStorageTemperatureCelsius();
            response.requiresRefrigeration = perishable.isRequiresRefrigeration();
            response.nearExpiry = perishable.isNearExpiry(30); // within 30 days
            response.remainingShelfLifeDays = perishable.getRemainingShelfLifeDays();
        }

        // Populate non-perishable-specific fields
        if (product instanceof NonPerishableProduct nonPerishable) {
            response.warrantyMonths = nonPerishable.getWarrantyMonths();
            response.weightKg = nonPerishable.getWeightKg();
            response.fragile = nonPerishable.isFragile();
        }

        return response;
    }

    // --- Getters and Setters ---

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public int getExpiredQuantity() {
        return expiredQuantity;
    }

    public void setExpiredQuantity(int expiredQuantity) {
        this.expiredQuantity = expiredQuantity;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(double profitMargin) {
        this.profitMargin = profitMargin;
    }

    public String getStorageRequirement() {
        return storageRequirement;
    }

    public void setStorageRequirement(String storageRequirement) {
        this.storageRequirement = storageRequirement;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Double getStorageTemperatureCelsius() {
        return storageTemperatureCelsius;
    }

    public void setStorageTemperatureCelsius(Double storageTemperatureCelsius) {
        this.storageTemperatureCelsius = storageTemperatureCelsius;
    }

    public Boolean getRequiresRefrigeration() {
        return requiresRefrigeration;
    }

    public void setRequiresRefrigeration(Boolean requiresRefrigeration) {
        this.requiresRefrigeration = requiresRefrigeration;
    }

    public Boolean getNearExpiry() {
        return nearExpiry;
    }

    public void setNearExpiry(Boolean nearExpiry) {
        this.nearExpiry = nearExpiry;
    }

    public Long getRemainingShelfLifeDays() {
        return remainingShelfLifeDays;
    }

    public void setRemainingShelfLifeDays(Long remainingShelfLifeDays) {
        this.remainingShelfLifeDays = remainingShelfLifeDays;
    }

    public Integer getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setWarrantyMonths(Integer warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public Boolean getFragile() {
        return fragile;
    }

    public void setFragile(Boolean fragile) {
        this.fragile = fragile;
    }
}
