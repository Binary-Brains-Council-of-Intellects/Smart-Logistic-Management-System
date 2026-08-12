package com.binarybrains.slms.inventory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ====================================================================
 * ABSTRACT CLASS: Product
 * ====================================================================
 * OOP Concepts demonstrated:
 *   - ABSTRACTION: Cannot be instantiated directly.
 *   - ENCAPSULATION: All fields are private with controlled access.
 *   - INHERITANCE: PerishableProduct and NonPerishableProduct extend this.
 *   - POLYMORPHISM: calculateStorageRequirement() is abstract,
 *     overridden differently by each subclass.
 *   - METHOD OVERLOADING: addStock(int) and addStock(int, String).
 *
 * MongoDB: Stored in a single "products" collection. Spring Data MongoDB
 * uses the "_class" discriminator field to identify which Java subclass
 * each document maps to. This preserves the inheritance hierarchy in
 * the database.
 * ====================================================================
 */
@Document(collection = "products")
public abstract class Product {

    @Id
    private String productId;

    @Field("name")
    private String name;

    @Indexed(unique = true)
    @Field("sku")
    private String sku;

    @Field("description")
    private String description;

    @Indexed
    @Field("category")
    private ProductCategory category;

    @Indexed
    @Field("product_type")
    private ProductType productType;

    @Field("batch_number")
    private String batchNumber;

    @Field("production_date")
    private LocalDate productionDate;

    @Field("expiry_date")
    private LocalDate expiryDate;

    @Field("total_quantity")
    private int totalQuantity;

    @Field("available_quantity")
    private int availableQuantity;

    @Field("expired_quantity")
    private int expiredQuantity;

    @Field("cost_price")
    private double costPrice;

    @Field("selling_price")
    private double sellingPrice;

    @Field("active")
    private boolean active;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    // ====================================================================
    // CONSTRUCTORS
    // ====================================================================

    /**
     * No-arg constructor required by Spring Data MongoDB for deserialization.
     */
    protected Product() {
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Full constructor for creating a product with all required fields.
     */
    protected Product(String name, String sku, String description, ProductCategory category,
                      ProductType productType, String batchNumber, LocalDate productionDate,
                      LocalDate expiryDate, int totalQuantity, double costPrice, double sellingPrice) {
        this();
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.category = category;
        this.productType = productType;
        this.batchNumber = batchNumber;
        this.productionDate = productionDate;
        this.expiryDate = expiryDate;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = totalQuantity;
        this.expiredQuantity = 0;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
    }

    // ====================================================================
    // DOMAIN BEHAVIOR — ENCAPSULATION
    // Stock is modified only through these controlled methods,
    // never by directly calling setAvailableQuantity().
    // ====================================================================

    /**
     * Adds stock to this product.
     * Demonstrates ENCAPSULATION — stock modification goes through
     * domain logic that enforces business rules.
     *
     * @param quantity the amount to add (must be positive)
     * @throws IllegalArgumentException if quantity is not positive
     */
    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Stock quantity to add must be positive");
        }
        this.availableQuantity += quantity;
        this.totalQuantity += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * METHOD OVERLOADING: Adds stock with batch information.
     *
     * @param quantity    the amount to add
     * @param batchNumber the batch number for the new stock
     */
    public void addStock(int quantity, String batchNumber) {
        addStock(quantity);
        this.batchNumber = batchNumber;
    }

    /**
     * Dispatches (deducts) stock from this product.
     * Enforces that available quantity cannot go negative.
     *
     * @param quantity the amount to dispatch (must be positive)
     * @throws IllegalArgumentException if quantity is not positive
     * @throws IllegalStateException    if insufficient stock is available
     */
    public void dispatch(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Dispatch quantity must be positive");
        }
        if (quantity > this.availableQuantity) {
            throw new IllegalStateException(
                    String.format("Cannot dispatch %d units. Only %d available for product '%s'",
                            quantity, this.availableQuantity, this.name));
        }
        this.availableQuantity -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Checks if this product has expired based on its expiry date.
     *
     * @return true if the product is past its expiry date
     */
    public boolean isExpired() {
        if (this.expiryDate == null) {
            return false;
        }
        return LocalDate.now().isAfter(this.expiryDate);
    }

    /**
     * Calculates the profit margin as a percentage.
     *
     * @return the profit margin percentage, or 0 if cost price is zero
     */
    public double calculateProfitMargin() {
        if (this.costPrice <= 0) {
            return 0.0;
        }
        return ((this.sellingPrice - this.costPrice) / this.costPrice) * 100.0;
    }

    /**
     * ABSTRACT METHOD — must be overridden by each subclass.
     * Demonstrates ABSTRACTION + POLYMORPHISM.
     *
     * Perishable products require cold/specialized storage.
     * Non-perishable products use standard warehousing.
     *
     * @return a description of the storage requirement
     */
    public abstract String calculateStorageRequirement();

    /**
     * Marks expired quantity and reduces available stock.
     *
     * @param quantity the number of units that have expired
     */
    public void markExpired(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Expired quantity must be positive");
        }
        if (quantity > this.availableQuantity) {
            quantity = this.availableQuantity;
        }
        this.availableQuantity -= quantity;
        this.expiredQuantity += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Deactivates this product (soft delete).
     */
    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Reactivates this product.
     */
    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    // ====================================================================
    // GETTERS — controlled read access to private fields
    // ====================================================================

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getSku() {
        return sku;
    }

    public String getDescription() {
        return description;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public ProductType getProductType() {
        return productType;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public int getExpiredQuantity() {
        return expiredQuantity;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // ====================================================================
    // SETTERS — only for fields that genuinely need external modification.
    // Stock quantities are NOT settable directly — use addStock()/dispatch().
    // ====================================================================

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return String.format("Product{id='%s', name='%s', sku='%s', type=%s, available=%d}",
                productId, name, sku, productType, availableQuantity);
    }
}
