package com.binarybrains.slms.inventory.dto;

import com.binarybrains.slms.inventory.model.ProductCategory;
import com.binarybrains.slms.inventory.model.ProductType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * DTO for creating a new product.
 * Uses Jakarta Bean Validation to enforce input constraints.
 */
public class CreateProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "SKU is required")
    @Size(min = 3, max = 50, message = "SKU must be between 3 and 50 characters")
    private String sku;

    private String description;

    @NotNull(message = "Product category is required")
    private ProductCategory category;

    @NotNull(message = "Product type is required")
    private ProductType productType;

    private String batchNumber;

    private LocalDate productionDate;

    private LocalDate expiryDate;

    @PositiveOrZero(message = "Quantity must be zero or positive")
    private int quantity;

    @Positive(message = "Cost price must be positive")
    private double costPrice;

    @Positive(message = "Selling price must be positive")
    private double sellingPrice;

    // Perishable-specific fields
    private Double storageTemperatureCelsius;
    private Boolean requiresRefrigeration;

    // Non-perishable-specific fields
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
