package com.binarybrains.slms.review.dto;

import com.binarybrains.slms.review.model.Review;
import java.time.LocalDateTime;

public class ReviewResponse {
    private String id;
    private String customerId;
    private String customerName;
    private String productId;
    private String productName;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    public static ReviewResponse fromReview(Review r) {
        ReviewResponse res = new ReviewResponse();
        res.id = r.getId(); res.customerId = r.getCustomerId(); res.customerName = r.getCustomerName();
        res.productId = r.getProductId(); res.productName = r.getProductName();
        res.rating = r.getRating(); res.comment = r.getComment(); res.createdAt = r.getCreatedAt();
        return res;
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
