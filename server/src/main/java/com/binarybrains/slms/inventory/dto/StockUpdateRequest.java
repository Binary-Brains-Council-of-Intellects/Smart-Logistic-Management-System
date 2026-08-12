package com.binarybrains.slms.inventory.dto;

import jakarta.validation.constraints.Positive;

/**
 * DTO for stock update operations (add stock / deduct stock).
 */
public class StockUpdateRequest {

    @Positive(message = "Quantity must be positive")
    private int quantity;

    private String batchNumber;

    public StockUpdateRequest() {
    }

    public StockUpdateRequest(int quantity) {
        this.quantity = quantity;
    }

    public StockUpdateRequest(int quantity, String batchNumber) {
        this.quantity = quantity;
        this.batchNumber = batchNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }
}
