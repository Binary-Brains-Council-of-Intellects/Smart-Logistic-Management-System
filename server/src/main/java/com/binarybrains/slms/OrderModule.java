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
 * Order & Sales Management Module
 */
public class OrderModule {

    // Abstraction
    public interface Billable {
        double calculateBillableAmount();
        void recalculateTotal();
    }

    public enum OrderStatus {
        PENDING, CONFIRMED, DISPATCHED, CANCELLED
    }

    // Encapsulation
    public static class OrderItem {

        // Encapsulation
        private String productId;
        private String productName;
        private int quantity;
        private double unitPrice;
        private double subtotal;

        // Polymorphism
        public OrderItem() {}

        // Polymorphism
        public OrderItem(String productId, String productName, int quantity, double unitPrice) {
            this.productId = productId;
            this.productName = productName;
            setQuantity(quantity);
            setUnitPrice(unitPrice);
        }

        // Polymorphism
        public OrderItem(String productId, String productName, int quantity) {
            this(productId, productName, quantity, 0.0);
        }

        private void recalculateSubtotal() {
            this.subtotal = this.quantity * this.unitPrice;
        }

        // Encapsulation
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) {
            this.quantity = Math.max(0, quantity);
            recalculateSubtotal();
        }

        public double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(double unitPrice) {
            this.unitPrice = Math.max(0.0, unitPrice);
            recalculateSubtotal();
        }

        public double getSubtotal() { return subtotal; }
        public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    }

    // Inheritance & Composition
    public static class Order implements Billable {

        // Encapsulation
        private String id;
        private String customerName;
        private String orderDate;
        private OrderStatus status;

        // Composition
        private List<OrderItem> items = new ArrayList<>();
        private double totalAmount;

        // Polymorphism
        public Order() {}

        // Polymorphism
        public Order(String id, String customerName, String orderDate, OrderStatus status, List<OrderItem> items) {
            this.id = id;
            this.customerName = customerName;
            this.orderDate = orderDate;
            this.status = status;
            this.items = items != null ? items : new ArrayList<>();
            recalculateTotal();
        }

        // Polymorphism
        public Order(String id, String customerName, String orderDate) {
            this(id, customerName, orderDate, OrderStatus.PENDING, new ArrayList<>());
        }

        // Polymorphism
        @Override
        public double calculateBillableAmount() {
            recalculateTotal();
            return this.totalAmount;
        }

        // Polymorphism
        @Override
        public void recalculateTotal() {
            if (this.items != null) {
                this.totalAmount = this.items.stream()
                        .mapToDouble(OrderItem::getSubtotal)
                        .sum();
            } else {
                this.totalAmount = 0.0;
            }
        }

        // Polymorphism
        public void addItem(OrderItem item) {
            if (item != null) {
                if (this.items == null) this.items = new ArrayList<>();
                this.items.add(item);
                recalculateTotal();
            }
        }

        // Polymorphism
        public void addItem(String productId, String productName, int quantity, double unitPrice) {
            addItem(new OrderItem(productId, productName, quantity, unitPrice));
        }

        // Encapsulation
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

    // Abstraction
    public interface OrderRepository {
        List<Order> findAll();
        Optional<Order> findById(String id);
        List<Order> findByStatus(OrderStatus status);
        Order save(Order order);
    }

    // Inheritance
    public static class InMemoryOrderRepository implements OrderRepository {
        private final Map<String, Order> store = new ConcurrentHashMap<>();

        public InMemoryOrderRepository() {
            List<OrderItem> items1 = new ArrayList<>();
            items1.add(new OrderItem("PRD-1001", "Wireless Ergonomic Mouse", 2, 35.0));
            save(new Order("ORD-9001", "Apex Logistics Ltd", "2026-08-14", OrderStatus.CONFIRMED, items1));

            List<OrderItem> items2 = new ArrayList<>();
            items2.add(new OrderItem("PRD-1002", "High-Speed USB-C Cable 2m", 5, 12.0));
            save(new Order("ORD-9002", "Global Retail Inc", "2026-08-15", OrderStatus.PENDING, items2));
        }

        @Override public List<Order> findAll() { return new ArrayList<>(store.values()); }
        @Override public Optional<Order> findById(String id) { return Optional.ofNullable(store.get(id)); }
        @Override public List<Order> findByStatus(OrderStatus status) {
            List<Order> list = new ArrayList<>();
            for (Order o : store.values()) if (o.getStatus() == status) list.add(o);
            return list;
        }
        @Override public Order save(Order order) { store.put(order.getId(), order); return order; }
    }

    // Composition
    public static class OrderHttpHandler implements HttpHandler {
        private final OrderRepository orderRepository;

        public OrderHttpHandler(OrderRepository orderRepository) {
            this.orderRepository = orderRepository;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PATCH, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");

            String method = exchange.getRequestMethod();
            if ("OPTIONS".equalsIgnoreCase(method)) { exchange.sendResponseHeaders(204, -1); return; }

            String path = exchange.getRequestURI().getPath();
            String[] parts = path.split("/");
            String id = (parts.length > 3) ? parts[3] : null;

            try {
                if ("GET".equalsIgnoreCase(method)) {
                    if (id != null) {
                        Optional<Order> ord = orderRepository.findById(id);
                        if (ord.isPresent()) sendJsonResponse(exchange, 200, SlmsApplication.toJson(ord.get()));
                        else sendJsonResponse(exchange, 404, "{\"error\":\"Order not found\"}");
                    } else {
                        sendJsonResponse(exchange, 200, SlmsApplication.toJson(orderRepository.findAll()));
                    }
                } else if ("POST".equalsIgnoreCase(method)) {
                    String body = readBody(exchange);
                    Map<String, String> map = SlmsApplication.parseJsonMap(body);
                    Order order = new Order();
                    order.setId(map.containsKey("id") && !map.get("id").isEmpty() ? map.get("id") : "ORD-" + (int)(9000 + Math.random() * 1000));
                    order.setCustomerName(map.getOrDefault("customerName", "Walk-in Customer"));
                    order.setOrderDate(map.getOrDefault("orderDate", "2026-08-16"));
                    try { order.setStatus(OrderStatus.valueOf(map.getOrDefault("status", "PENDING"))); } catch (Exception e) { order.setStatus(OrderStatus.PENDING); }
                    order.recalculateTotal();
                    Order saved = orderRepository.save(order);
                    sendJsonResponse(exchange, 200, SlmsApplication.toJson(saved));
                } else if ("PATCH".equalsIgnoreCase(method)) {
                    if (id != null) {
                        Optional<Order> ordOpt = orderRepository.findById(id);
                        if (ordOpt.isPresent()) {
                            String query = exchange.getRequestURI().getQuery();
                            if (query != null && query.contains("status=")) {
                                String statusStr = query.split("status=")[1].split("&")[0];
                                Order order = ordOpt.get();
                                try { order.setStatus(OrderStatus.valueOf(statusStr)); } catch (Exception ignored) {}
                                orderRepository.save(order);
                                sendJsonResponse(exchange, 200, SlmsApplication.toJson(order));
                                return;
                            }
                        }
                    }
                    sendJsonResponse(exchange, 404, "{\"error\":\"Order not found or invalid status\"}");
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"error\":\"" + e.getMessage() + "\"}");
            }
        }

        private String readBody(HttpExchange exchange) throws IOException {
            InputStream is = exchange.getRequestBody();
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }

        private void sendJsonResponse(HttpExchange exchange, int statusCode, String json) throws IOException {
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }
}
