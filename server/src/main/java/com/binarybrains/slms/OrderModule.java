package com.binarybrains.slms;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * MODULE 2: ORDER & SALES MANAGEMENT
 * Contains: Order Model, Order Item, OrderStatus Enum, Repository, and REST Controller (/api/orders).
 */
public class OrderModule {

    // ------------------------------------------------------------------------
    // 1. ENUMS & DATA MODELS
    // ------------------------------------------------------------------------

    public enum OrderStatus {
        PENDING, CONFIRMED, DISPATCHED, CANCELLED
    }

    public static class OrderItem {
        private String productId;
        private String productName;
        private int quantity;
        private double unitPrice;
        private double subtotal;

        public OrderItem() {}

        public OrderItem(String productId, String productName, int quantity, double unitPrice) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.subtotal = quantity * unitPrice;
        }

        // Getters and Setters
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) {
            this.quantity = quantity;
            this.subtotal = this.quantity * this.unitPrice;
        }

        public double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
            this.subtotal = this.quantity * this.unitPrice;
        }

        public double getSubtotal() { return subtotal; }
        public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    }

    @Document(collection = "orders")
    public static class Order {
        @Id
        private String id;
        private String customerName;
        private String orderDate;
        private OrderStatus status;
        private List<OrderItem> items = new ArrayList<>();
        private double totalAmount;

        public Order() {}

        public Order(String id, String customerName, String orderDate, OrderStatus status, List<OrderItem> items) {
            this.id = id;
            this.customerName = customerName;
            this.orderDate = orderDate;
            this.status = status;
            this.items = items != null ? items : new ArrayList<>();
            recalculateTotal();
        }

        public void recalculateTotal() {
            if (this.items != null) {
                this.totalAmount = this.items.stream()
                        .mapToDouble(OrderItem::getSubtotal)
                        .sum();
            } else {
                this.totalAmount = 0.0;
            }
        }

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }

        public String getOrderDate() { return orderDate; }
        public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

        public OrderStatus getStatus() { return status; }
        public void setStatus(OrderStatus status) { this.status = status; }

        public List<OrderItem> getItems() { return items; }
        public void setItems(List<OrderItem> items) {
            this.items = items;
            recalculateTotal();
        }

        public double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    }

    // ------------------------------------------------------------------------
    // 2. MONGO REPOSITORY
    // ------------------------------------------------------------------------

    public interface OrderRepository extends MongoRepository<Order, String> {
        List<Order> findByStatus(OrderStatus status);
        List<Order> findByCustomerNameContainingIgnoreCase(String customerName);
    }

    // ------------------------------------------------------------------------
    // 3. REST CONTROLLER
    // ------------------------------------------------------------------------

    @RestController
    @RequestMapping("/api/orders")
    @CrossOrigin(origins = "*")
    public static class OrderController {

        private final OrderRepository orderRepository;

        public OrderController(OrderRepository orderRepository) {
            this.orderRepository = orderRepository;
        }

        @GetMapping
        public List<Order> getAllOrders() {
            return orderRepository.findAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity<Order> getOrderById(@PathVariable String id) {
            Optional<Order> order = orderRepository.findById(id);
            return order.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
        }

        @PostMapping
        public ResponseEntity<Order> createOrder(@RequestBody Order order) {
            if (order.getId() == null || order.getId().isEmpty()) {
                order.setId("ORD-" + (int)(9000 + Math.random() * 1000));
            }
            if (order.getStatus() == null) {
                order.setStatus(OrderStatus.PENDING);
            }
            order.recalculateTotal();
            Order savedOrder = orderRepository.save(order);
            return ResponseEntity.ok(savedOrder);
        }

        @PatchMapping("/{id}/status")
        public ResponseEntity<Order> updateOrderStatus(@PathVariable String id, @RequestParam OrderStatus status) {
            Optional<Order> orderOpt = orderRepository.findById(id);
            if (orderOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Order order = orderOpt.get();
            order.setStatus(status);
            Order updated = orderRepository.save(order);
            return ResponseEntity.ok(updated);
        }
    }
}
