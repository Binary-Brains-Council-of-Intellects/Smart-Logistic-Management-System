package com.binarybrains.slms.returns.dto;

import com.binarybrains.slms.returns.model.ReturnReason;
import com.binarybrains.slms.returns.model.ReturnRequest;
import com.binarybrains.slms.returns.model.ReturnStatus;
import java.time.LocalDateTime;

public class ReturnResponse {
    private String id;
    private String orderId;
    private String customerId;
    private String customerName;
    private String productId;
    private String productName;
    private int quantity;
    private ReturnReason reason;
    private ReturnStatus status;
    private boolean canRestoreToInventory;
    private LocalDateTime requestDate;
    private LocalDateTime resolvedDate;
    private String notes;

    public static ReturnResponse fromReturnRequest(ReturnRequest r) {
        ReturnResponse res = new ReturnResponse();
        res.id = r.getId(); res.orderId = r.getOrderId(); res.customerId = r.getCustomerId();
        res.customerName = r.getCustomerName(); res.productId = r.getProductId();
        res.productName = r.getProductName(); res.quantity = r.getQuantity();
        res.reason = r.getReason(); res.status = r.getStatus();
        res.canRestoreToInventory = r.canRestoreToInventory();
        res.requestDate = r.getRequestDate(); res.resolvedDate = r.getResolvedDate();
        res.notes = r.getNotes();
        return res;
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
    public boolean isCanRestoreToInventory() { return canRestoreToInventory; }
    public void setCanRestoreToInventory(boolean canRestoreToInventory) { this.canRestoreToInventory = canRestoreToInventory; }
    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }
    public LocalDateTime getResolvedDate() { return resolvedDate; }
    public void setResolvedDate(LocalDateTime resolvedDate) { this.resolvedDate = resolvedDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
