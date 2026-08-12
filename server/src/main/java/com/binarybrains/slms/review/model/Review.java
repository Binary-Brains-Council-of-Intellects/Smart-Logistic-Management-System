package com.binarybrains.slms.review.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "reviews")
public class Review {

    @Id
    private String id;

    @Indexed
    @Field("customer_id")
    private String customerId;

    @Field("customer_name")
    private String customerName;

    @Indexed
    @Field("product_id")
    private String productId;

    @Field("product_name")
    private String productName;

    @Field("rating")
    private int rating; // 1-5

    @Field("comment")
    private String comment;

    @Field("created_at")
    private LocalDateTime createdAt;

    public Review() { this.createdAt = LocalDateTime.now(); }

    public Review(String customerId, String customerName, String productId,
                  String productName, int rating, String comment) {
        this();
        this.customerId = customerId; this.customerName = customerName;
        this.productId = productId; this.productName = productName;
        this.rating = rating; this.comment = comment;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
