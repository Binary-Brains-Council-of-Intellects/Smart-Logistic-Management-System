package com.binarybrains.slms;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Customer Feedback & Returns Module
 */
public class ReviewModule {

    // Abstraction
    public static abstract class FeedbackEntity {

        // Encapsulation
        private String id;
        private String productId;
        private String productName;
        private String customerName;

        public FeedbackEntity() {}

        // Polymorphism
        public FeedbackEntity(String id, String productId, String productName, String customerName) {
            this.id = id;
            this.productId = productId;
            this.productName = productName;
            this.customerName = customerName;
        }

        // Encapsulation
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }

        // Abstraction
        public abstract String getFeedbackSummary();
    }

    public enum ExchangeReason {
        WRONG_PRODUCT, DAMAGED, EXPIRED_ON_ARRIVAL, OTHER
    }

    // Inheritance
    public static class Review extends FeedbackEntity {

        // Encapsulation
        private int rating;
        private String reviewDate;
        private String comment;

        // Polymorphism
        public Review() { super(); }

        // Polymorphism
        public Review(String id, String productId, String productName, String customerName,
                      int rating, String reviewDate, String comment) {
            super(id, productId, productName, customerName);
            setRating(rating);
            this.reviewDate = reviewDate;
            this.comment = comment;
        }

        // Polymorphism
        @Override
        public String getFeedbackSummary() {
            return "Review [" + getId() + "] Product: " + getProductName() + " | Rating: " + rating + "/5 stars";
        }

        // Encapsulation
        public int getRating() { return rating; }
        public void setRating(int rating) {
            this.rating = Math.min(5, Math.max(1, rating));
        }

        public String getReviewDate() { return reviewDate; }
        public void setReviewDate(String reviewDate) { this.reviewDate = reviewDate; }

        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
    }

    // Inheritance
    public static class ReturnRequest extends FeedbackEntity {

        // Encapsulation
        private String orderId;
        private ExchangeReason reason;
        private String exchangeDate;
        private String status;
        private String notes;

        // Polymorphism
        public ReturnRequest() { super(); }

        // Polymorphism
        public ReturnRequest(String id, String orderId, String productId, String productName,
                             String customerName, ExchangeReason reason, String exchangeDate,
                             String status, String notes) {
            super(id, productId, productName, customerName);
            this.orderId = orderId;
            this.reason = reason;
            this.exchangeDate = exchangeDate;
            this.status = status;
            this.notes = notes;
        }

        // Polymorphism
        @Override
        public String getFeedbackSummary() {
            return "Return Request [" + getId() + "] Order: " + orderId + " | Reason: " + reason + " | Status: " + status;
        }

        // Encapsulation
        public String getOrderId() { return orderId; }
        public void setOrderId(String orderId) { this.orderId = orderId; }

        public ExchangeReason getReason() { return reason; }
        public void setReason(ExchangeReason reason) { this.reason = reason; }

        public String getExchangeDate() { return exchangeDate; }
        public void setExchangeDate(String exchangeDate) { this.exchangeDate = exchangeDate; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    // Abstraction
    public interface ReviewRepository {
        List<Review> findAll();
        List<Review> findByProductId(String productId);
        Review save(Review review);
    }

    // Abstraction
    public interface ReturnRepository {
        List<ReturnRequest> findAll();
        List<ReturnRequest> findByReason(ExchangeReason reason);
        ReturnRequest save(ReturnRequest request);
    }

    // Inheritance
    public static class InMemoryReviewRepository implements ReviewRepository {
        private final Map<String, Review> store = new ConcurrentHashMap<>();

        public InMemoryReviewRepository() {
            save(new Review("REV-301", "PRD-1001", "Wireless Ergonomic Mouse", "Saima Khan", 5, "2026-08-10", "Excellent mouse, very smooth!"));
            save(new Review("REV-302", "PRD-1002", "High-Speed USB-C Cable 2m", "Tariq Hasan", 4, "2026-08-12", "Durable build quality."));
        }

        @Override public List<Review> findAll() { return new ArrayList<>(store.values()); }
        @Override public List<Review> findByProductId(String productId) {
            List<Review> list = new ArrayList<>();
            for (Review r : store.values()) if (Objects.equals(r.getProductId(), productId)) list.add(r);
            return list;
        }
        @Override public Review save(Review review) { store.put(review.getId(), review); return review; }
    }

    // Inheritance
    public static class InMemoryReturnRepository implements ReturnRepository {
        private final Map<String, ReturnRequest> store = new ConcurrentHashMap<>();

        public InMemoryReturnRepository() {
            save(new ReturnRequest("EXC-401", "ORD-9001", "PRD-1003", "Whole Milk Powder (1kg)", "Tanvir Hossain", ExchangeReason.DAMAGED, "2026-08-11", "Approved", "Outer seal packaging torn."));
        }

        @Override public List<ReturnRequest> findAll() { return new ArrayList<>(store.values()); }
        @Override public List<ReturnRequest> findByReason(ExchangeReason reason) {
            List<ReturnRequest> list = new ArrayList<>();
            for (ReturnRequest req : store.values()) if (req.getReason() == reason) list.add(req);
            return list;
        }
        @Override public ReturnRequest save(ReturnRequest request) { store.put(request.getId(), request); return request; }
    }

    // Composition
    public static class ReviewHttpHandler implements HttpHandler {
        private final ReviewRepository reviewRepository;

        public ReviewHttpHandler(ReviewRepository reviewRepository) {
            this.reviewRepository = reviewRepository;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setupCors(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) { exchange.sendResponseHeaders(204, -1); return; }

            String method = exchange.getRequestMethod();
            if ("GET".equalsIgnoreCase(method)) {
                sendJsonResponse(exchange, 200, SlmsApplication.toJson(reviewRepository.findAll()));
            } else if ("POST".equalsIgnoreCase(method)) {
                String body = readBody(exchange);
                Map<String, String> map = SlmsApplication.parseJsonMap(body);
                Review rev = new Review();
                rev.setId(map.containsKey("id") && !map.get("id").isEmpty() ? map.get("id") : "REV-" + (int)(300 + Math.random() * 700));
                rev.setProductId(map.getOrDefault("productId", "PRD-1001"));
                rev.setProductName(map.getOrDefault("productName", "Product"));
                rev.setCustomerName(map.getOrDefault("customerName", "Customer"));
                rev.setRating(map.containsKey("rating") ? Integer.parseInt(map.get("rating")) : 5);
                rev.setReviewDate(map.getOrDefault("reviewDate", "2026-08-16"));
                rev.setComment(map.getOrDefault("comment", "Great product!"));
                Review saved = reviewRepository.save(rev);
                sendJsonResponse(exchange, 200, SlmsApplication.toJson(saved));
            }
        }
    }

    // Composition
    public static class ReturnHttpHandler implements HttpHandler {
        private final ReturnRepository returnRepository;

        public ReturnHttpHandler(ReturnRepository returnRepository) {
            this.returnRepository = returnRepository;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setupCors(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) { exchange.sendResponseHeaders(204, -1); return; }

            String method = exchange.getRequestMethod();
            if ("GET".equalsIgnoreCase(method)) {
                sendJsonResponse(exchange, 200, SlmsApplication.toJson(returnRepository.findAll()));
            } else if ("POST".equalsIgnoreCase(method)) {
                String body = readBody(exchange);
                Map<String, String> map = SlmsApplication.parseJsonMap(body);
                ReturnRequest req = new ReturnRequest();
                req.setId(map.containsKey("id") && !map.get("id").isEmpty() ? map.get("id") : "EXC-" + (int)(400 + Math.random() * 600));
                req.setOrderId(map.getOrDefault("orderId", "ORD-9001"));
                req.setProductId(map.getOrDefault("productId", "PRD-1001"));
                req.setProductName(map.getOrDefault("productName", "Product"));
                req.setCustomerName(map.getOrDefault("customerName", "Customer"));
                try { req.setReason(ExchangeReason.valueOf(map.getOrDefault("reason", "OTHER"))); } catch (Exception e) { req.setReason(ExchangeReason.OTHER); }
                req.setExchangeDate(map.getOrDefault("exchangeDate", "2026-08-16"));
                req.setStatus(map.getOrDefault("status", "Pending Inspection"));
                req.setNotes(map.getOrDefault("notes", ""));
                ReturnRequest saved = returnRepository.save(req);
                sendJsonResponse(exchange, 200, SlmsApplication.toJson(saved));
            }
        }
    }

    private static void setupCors(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
    }

    private static String readBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    private static void sendJsonResponse(HttpExchange exchange, int statusCode, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
