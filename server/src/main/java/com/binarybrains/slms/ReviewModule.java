package com.binarybrains.slms;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MODULE 4: CUSTOMER FEEDBACK & RETURNS
 * Contains: Customer Review, Return/Exchange Data Models, Repositories, and REST Controllers.
 */
public class ReviewModule {

    // ------------------------------------------------------------------------
    // 1. ENUMS & DATA MODELS
    // ------------------------------------------------------------------------

    public enum ExchangeReason {
        WRONG_PRODUCT, DAMAGED, EXPIRED_ON_ARRIVAL, OTHER
    }

    @Document(collection = "reviews")
    public static class Review {
        @Id
        private String id;
        private String productId;
        private String productName;
        private String customerName;
        private int rating; // 1 to 5 stars
        private String reviewDate;
        private String comment;

        public Review() {}

        public Review(String id, String productId, String productName, String customerName,
                      int rating, String reviewDate, String comment) {
            this.id = id;
            this.productId = productId;
            this.productName = productName;
            this.customerName = customerName;
            this.rating = rating;
            this.reviewDate = reviewDate;
            this.comment = comment;
        }

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }

        public int getRating() { return rating; }
        public void setRating(int rating) { this.rating = rating; }

        public String getReviewDate() { return reviewDate; }
        public void setReviewDate(String reviewDate) { this.reviewDate = reviewDate; }

        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
    }

    @Document(collection = "exchanges")
    public static class ReturnRequest {
        @Id
        private String id;
        private String orderId;
        private String productId;
        private String productName;
        private String customerName;
        private ExchangeReason reason;
        private String exchangeDate;
        private String status;
        private String notes;

        public ReturnRequest() {}

        public ReturnRequest(String id, String orderId, String productId, String productName,
                             String customerName, ExchangeReason reason, String exchangeDate,
                             String status, String notes) {
            this.id = id;
            this.orderId = orderId;
            this.productId = productId;
            this.productName = productName;
            this.customerName = customerName;
            this.reason = reason;
            this.exchangeDate = exchangeDate;
            this.status = status;
            this.notes = notes;
        }

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getOrderId() { return orderId; }
        public void setOrderId(String orderId) { this.orderId = orderId; }

        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }

        public ExchangeReason getReason() { return reason; }
        public void setReason(ExchangeReason reason) { this.reason = reason; }

        public String getExchangeDate() { return exchangeDate; }
        public void setExchangeDate(String exchangeDate) { this.exchangeDate = exchangeDate; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    // ------------------------------------------------------------------------
    // 2. MONGO REPOSITORIES
    // ------------------------------------------------------------------------

    public interface ReviewRepository extends MongoRepository<Review, String> {
        List<Review> findByProductId(String productId);
    }

    public interface ReturnRepository extends MongoRepository<ReturnRequest, String> {
        List<ReturnRequest> findByReason(ExchangeReason reason);
    }

    // ------------------------------------------------------------------------
    // 3. REST CONTROLLERS
    // ------------------------------------------------------------------------

    @RestController
    @RequestMapping("/api/reviews")
    @CrossOrigin(origins = "*")
    public static class ReviewController {

        private final ReviewRepository reviewRepository;

        public ReviewController(ReviewRepository reviewRepository) {
            this.reviewRepository = reviewRepository;
        }

        @GetMapping
        public List<Review> getAllReviews() {
            return reviewRepository.findAll();
        }

        @PostMapping
        public ResponseEntity<Review> createReview(@RequestBody Review review) {
            if (review.getId() == null || review.getId().isEmpty()) {
                review.setId("REV-" + (int)(300 + Math.random() * 700));
            }
            Review saved = reviewRepository.save(review);
            return ResponseEntity.ok(saved);
        }
    }

    @RestController
    @RequestMapping("/api/exchanges")
    @CrossOrigin(origins = "*")
    public static class ReturnController {

        private final ReturnRepository returnRepository;

        public ReturnController(ReturnRepository returnRepository) {
            this.returnRepository = returnRepository;
        }

        @GetMapping
        public List<ReturnRequest> getAllExchanges() {
            return returnRepository.findAll();
        }

        @PostMapping
        public ResponseEntity<ReturnRequest> createExchange(@RequestBody ReturnRequest request) {
            if (request.getId() == null || request.getId().isEmpty()) {
                request.setId("EXC-" + (int)(400 + Math.random() * 600));
            }
            if (request.getStatus() == null || request.getStatus().isEmpty()) {
                request.setStatus("Pending Inspection");
            }
            ReturnRequest saved = returnRepository.save(request);
            return ResponseEntity.ok(saved);
        }
    }
}
