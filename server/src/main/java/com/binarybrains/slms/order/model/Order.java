package com.binarybrains.slms.order.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order document.
 * OrderItems are EMBEDDED — a MongoDB best practice for 1:N relationships
 * where the children are always accessed with the parent.
 */
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    @Indexed
    @Field("customer_id")
    private String customerId;

    @Field("customer_name")
    private String customerName;

    @Field("items")
    private List<OrderItem> items;

    @Field("total_amount")
    private double totalAmount;

    @Indexed
    @Field("status")
    private OrderStatus status;

    @Field("pricing_strategy")
    private String pricingStrategy; // REGULAR, WHOLESALE, DISCOUNT

    @Indexed
    @Field("order_date")
    private LocalDateTime orderDate;

    @Field("notes")
    private String notes;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    public Order() {
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.orderDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.pricingStrategy = "REGULAR";
    }

    /**
     * Calculates the total order amount from all items.
     */
    public void calculateTotal() {
        this.totalAmount = items.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
        calculateTotal();
    }

    // --- Getters and Setters ---

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
    public void setStatus(OrderStatus status) { this.status = status; this.updatedAt = LocalDateTime.now(); }
    public String getPricingStrategy() { return pricingStrategy; }
    public void setPricingStrategy(String pricingStrategy) { this.pricingStrategy = pricingStrategy; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
