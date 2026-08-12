package com.binarybrains.slms.order.dto;

import com.binarybrains.slms.order.model.Order;
import com.binarybrains.slms.order.model.OrderItem;
import com.binarybrains.slms.order.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private String id;
    private String customerId;
    private String customerName;
    private List<OrderItem> items;
    private double totalAmount;
    private OrderStatus status;
    private String pricingStrategy;
    private LocalDateTime orderDate;
    private String notes;
    private LocalDateTime updatedAt;

    public static OrderResponse fromOrder(Order o) {
        OrderResponse r = new OrderResponse();
        r.id = o.getId(); r.customerId = o.getCustomerId(); r.customerName = o.getCustomerName();
        r.items = o.getItems(); r.totalAmount = o.getTotalAmount(); r.status = o.getStatus();
        r.pricingStrategy = o.getPricingStrategy(); r.orderDate = o.getOrderDate();
        r.notes = o.getNotes(); r.updatedAt = o.getUpdatedAt();
        return r;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public String getPricingStrategy() { return pricingStrategy; }
    public void setPricingStrategy(String pricingStrategy) { this.pricingStrategy = pricingStrategy; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
