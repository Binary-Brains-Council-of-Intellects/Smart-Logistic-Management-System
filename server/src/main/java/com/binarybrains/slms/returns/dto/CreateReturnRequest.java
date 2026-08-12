package com.binarybrains.slms.returns.dto;

import com.binarybrains.slms.returns.model.ReturnReason;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateReturnRequest {
    @NotBlank(message = "Order ID is required")
    private String orderId;
    @NotBlank(message = "Customer ID is required")
    private String customerId;
    @NotBlank(message = "Product ID is required")
    private String productId;
    @Positive(message = "Quantity must be positive")
    private int quantity;
    @NotNull(message = "Return reason is required")
    private ReturnReason reason;
    private String notes;

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public ReturnReason getReason() { return reason; }
    public void setReason(ReturnReason reason) { this.reason = reason; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
