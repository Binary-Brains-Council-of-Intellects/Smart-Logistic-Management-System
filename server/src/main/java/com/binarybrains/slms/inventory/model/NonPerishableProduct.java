package com.binarybrains.slms.inventory.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

/**
 * ====================================================================
 * CONCRETE CLASS: NonPerishableProduct
 * ====================================================================
 * OOP Concepts demonstrated:
 *   - INHERITANCE: Extends abstract Product class.
 *   - METHOD OVERRIDING: Overrides calculateStorageRequirement()
 *     with standard warehouse storage logic.
 *   - POLYMORPHISM: Same method produces different result than
 *     PerishableProduct.calculateStorageRequirement().
 *
 * Represents products that do not expire (electronics, clothing, etc).
 * Has different storage and warehousing characteristics.
 * ====================================================================
 */
@Document(collection = "products")
public class NonPerishableProduct extends Product {

    @Field("warranty_months")
    private int warrantyMonths;

    @Field("weight_kg")
    private double weightKg;

    @Field("is_fragile")
    private boolean fragile;

    // ====================================================================
    // CONSTRUCTORS
    // ====================================================================

    public NonPerishableProduct() {
        super();
    }

    public NonPerishableProduct(String name, String sku, String description, ProductCategory category,
                                String batchNumber, LocalDate productionDate,
                                int totalQuantity, double costPrice, double sellingPrice,
                                int warrantyMonths, double weightKg, boolean fragile) {
        super(name, sku, description, category, ProductType.NON_PERISHABLE,
                batchNumber, productionDate, null, totalQuantity, costPrice, sellingPrice);
        this.warrantyMonths = warrantyMonths;
        this.weightKg = weightKg;
        this.fragile = fragile;
    }

    // ====================================================================
    // METHOD OVERRIDING — Polymorphism
    // ====================================================================

    /**
     * Non-perishable products use standard warehouse storage.
     * Fragile items require special handling.
     * This is behaviorally different from PerishableProduct's implementation.
     */
    @Override
    public String calculateStorageRequirement() {
        StringBuilder requirement = new StringBuilder("STANDARD WAREHOUSE: ");
        if (fragile) {
            requirement.append("FRAGILE — handle with care. Padded shelving required. ");
        } else {
            requirement.append("Regular shelving. ");
        }
        if (weightKg > 50) {
            requirement.append("HEAVY ITEM — floor-level storage recommended. ");
        }
        if (warrantyMonths > 0) {
            requirement.append(String.format("Warranty: %d months.", warrantyMonths));
        }
        return requirement.toString().trim();
    }

    // ====================================================================
    // NON-PERISHABLE SPECIFIC DOMAIN METHODS
    // ====================================================================

    /**
     * Checks if the warranty is still valid from the production date.
     *
     * @return true if within warranty period, false otherwise
     */
    public boolean isUnderWarranty() {
        if (getProductionDate() == null || warrantyMonths <= 0) {
            return false;
        }
        LocalDate warrantyEnd = getProductionDate().plusMonths(warrantyMonths);
        return !LocalDate.now().isAfter(warrantyEnd);
    }

    // ====================================================================
    // GETTERS AND SETTERS
    // ====================================================================

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setWarrantyMonths(int warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public boolean isFragile() {
        return fragile;
    }

    public void setFragile(boolean fragile) {
        this.fragile = fragile;
    }

    @Override
    public String toString() {
        return String.format("NonPerishableProduct{id='%s', name='%s', sku='%s', warranty=%d months}",
                getProductId(), getName(), getSku(), warrantyMonths);
    }
}
