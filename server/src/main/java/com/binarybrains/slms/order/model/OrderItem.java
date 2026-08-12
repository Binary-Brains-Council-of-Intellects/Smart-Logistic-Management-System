package com.binarybrains.slms.order.model;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Embedded document within an Order.
 * MongoDB modeling decision: OrderItems are embedded (not referenced)
 * because they are always read/written with the parent Order and
 * have no independent lifecycle.
 */
public class OrderItem {

    @Field("product_id")
    private String productId;

    @Field("product_name")
    private String productName;

    @Field("quantity")
    private int quantity;

    @Field("unit_price")
    private double unitPrice;

    @Field("subtotal")
    private double subtotal;

    public OrderItem() {}

    public OrderItem(String productId, String productName, int quantity, double unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = quantity * unitPrice;
    }

    public void calculateSubtotal() {
        this.subtotal = this.quantity * this.unitPrice;
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
