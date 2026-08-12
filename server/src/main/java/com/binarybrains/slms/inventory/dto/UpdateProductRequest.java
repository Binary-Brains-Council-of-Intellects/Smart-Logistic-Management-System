package com.binarybrains.slms.inventory.dto;

import com.binarybrains.slms.inventory.model.ProductCategory;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * DTO for updating an existing product.
 * All fields are optional — only provided fields will be updated.
 */
public class UpdateProductRequest {

    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    private String description;

    private ProductCategory category;

    private String batchNumber;

    private LocalDate productionDate;

    private LocalDate expiryDate;

    @Positive(message = "Cost price must be positive")
    private Double costPrice;

    @Positive(message = "Selling price must be positive")
    private Double sellingPrice;

    // Perishable-specific
    private Double storageTemperatureCelsius;
    private Boolean requiresRefrigeration;

    // Non-perishable-specific
    private Integer warrantyMonths;
    private Double weightKg;
    private Boolean fragile;

    // --- Getters and Setters ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
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
