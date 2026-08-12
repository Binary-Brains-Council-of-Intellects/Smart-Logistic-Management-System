package com.binarybrains.slms.returns.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "returns")
public class ReturnRequest {

    @Id
    private String id;

    @Indexed
    @Field("order_id")
    private String orderId;

    @Indexed
    @Field("customer_id")
    private String customerId;

    @Field("customer_name")
    private String customerName;

    @Field("product_id")
    private String productId;

    @Field("product_name")
    private String productName;

    @Field("quantity")
    private int quantity;

    @Field("reason")
    private ReturnReason reason;

    @Indexed
    @Field("status")
    private ReturnStatus status;

    @Field("request_date")
    private LocalDateTime requestDate;

    @Field("resolved_date")
    private LocalDateTime resolvedDate;

    @Field("notes")
    private String notes;

    public ReturnRequest() {
        this.status = ReturnStatus.REQUESTED;
        this.requestDate = LocalDateTime.now();
    }

    public ReturnRequest(String orderId, String customerId, String customerName,
                         String productId, String productName, int quantity,
                         ReturnReason reason, String notes) {
        this();
        this.orderId = orderId; this.customerId = customerId; this.customerName = customerName;
        this.productId = productId; this.productName = productName;
        this.quantity = quantity; this.reason = reason; this.notes = notes;
    }

    /**
     * Determines if returned product can be added back to inventory.
     * DAMAGED and EXPIRED items should NOT become sellable stock.
     */
    public boolean canRestoreToInventory() {
        return reason != ReturnReason.DAMAGED && reason != ReturnReason.EXPIRED;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public ReturnReason getReason() { return reason; }
    public void setReason(ReturnReason reason) { this.reason = reason; }
    public ReturnStatus getStatus() { return status; }
    public void setStatus(ReturnStatus status) { this.status = status; }
    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }
    public LocalDateTime getResolvedDate() { return resolvedDate; }
    public void setResolvedDate(LocalDateTime resolvedDate) { this.resolvedDate = resolvedDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
