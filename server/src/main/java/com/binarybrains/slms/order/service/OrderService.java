package com.binarybrains.slms.order.service;

import com.binarybrains.slms.common.exception.*;
import com.binarybrains.slms.customer.model.Customer;
import com.binarybrains.slms.customer.repository.CustomerRepository;
import com.binarybrains.slms.inventory.model.Product;
import com.binarybrains.slms.inventory.repository.ProductRepository;
import com.binarybrains.slms.inventory.strategy.PricingContext;
import com.binarybrains.slms.order.dto.CreateOrderRequest;
import com.binarybrains.slms.order.dto.OrderResponse;
import com.binarybrains.slms.order.model.Order;
import com.binarybrains.slms.order.model.OrderItem;
import com.binarybrains.slms.order.model.OrderStatus;
import com.binarybrains.slms.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Order service integrating inventory, customer, and pricing strategy.
 *
 * Order creation workflow:
 *   1. Validate customer exists
 *   2. For each item:
 *      a. Validate product exists and is active
 *      b. Check product is not expired
 *      c. Check sufficient stock
 *      d. Apply pricing strategy
 *   3. Deduct stock from inventory
 *   4. Save and confirm order
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final PricingContext pricingContext;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository,
                        CustomerRepository customerRepository, PricingContext pricingContext) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.pricingContext = pricingContext;
    }

    /**
     * Creates an order with full validation, stock checking, pricing, and stock deduction.
     */
    public OrderResponse createOrder(CreateOrderRequest request) {
        // 1. Validate customer
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.getCustomerId()));

        String strategyName = request.getPricingStrategy() != null ? request.getPricingStrategy() : "REGULAR";

        // 2. Validate all items and build order items
        List<OrderItem> orderItems = new ArrayList<>();
        List<Product> productsToUpdate = new ArrayList<>();

        for (CreateOrderRequest.OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(itemReq.getProductId()));

            // Check if product is expired
            if (product.isExpired()) {
                throw new ExpiredProductException(product.getProductId());
            }

            // Check stock availability
            if (product.getAvailableQuantity() < itemReq.getQuantity()) {
                throw new InsufficientStockException(
                        product.getProductId(), itemReq.getQuantity(), product.getAvailableQuantity());
            }

            // Apply pricing strategy
            double unitPrice = pricingContext.calculatePrice(
                    strategyName, product.getSellingPrice(), 1);
            double subtotal = pricingContext.calculatePrice(
                    strategyName, product.getSellingPrice(), itemReq.getQuantity());

            OrderItem orderItem = new OrderItem(
                    product.getProductId(), product.getName(),
                    itemReq.getQuantity(), unitPrice);
            orderItem.setSubtotal(subtotal);
            orderItems.add(orderItem);

            // Prepare stock deduction
            product.dispatch(itemReq.getQuantity());
            productsToUpdate.add(product);
        }

        // 3. Deduct stock (save all products)
        productRepository.saveAll(productsToUpdate);

        // 4. Create and save order
        Order order = new Order();
        order.setCustomerId(customer.getId());
        order.setCustomerName(customer.getName());
        order.setItems(orderItems);
        order.setPricingStrategy(strategyName);
        order.setNotes(request.getNotes());
        order.setStatus(OrderStatus.CONFIRMED);
        order.calculateTotal();

        Order saved = orderRepository.save(order);
        return OrderResponse.fromOrder(saved);
    }

    public OrderResponse getOrderById(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return OrderResponse.fromOrder(order);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::fromOrder).collect(Collectors.toList());
    }

    public List<OrderResponse> getOrdersByCustomer(String customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(OrderResponse::fromOrder).collect(Collectors.toList());
    }

    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(OrderResponse::fromOrder).collect(Collectors.toList());
    }

    public List<OrderResponse> getOrdersByDateRange(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findByOrderDateBetween(start, end).stream()
                .map(OrderResponse::fromOrder).collect(Collectors.toList());
    }

    public OrderResponse updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        // Cannot change status of already cancelled or delivered orders
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update a cancelled order");
        }
        if (order.getStatus() == OrderStatus.DELIVERED && newStatus != OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot change status of a delivered order");
        }

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        // If cancelling, restore stock
        if (newStatus == OrderStatus.CANCELLED) {
            restoreStockForOrder(order);
        }

        return OrderResponse.fromOrder(orderRepository.save(order));
    }

    public OrderResponse cancelOrder(String orderId) {
        return updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }

    private void restoreStockForOrder(Order order) {
        for (OrderItem item : order.getItems()) {
            productRepository.findById(item.getProductId()).ifPresent(product -> {
                product.addStock(item.getQuantity());
                productRepository.save(product);
            });
        }
    }
}
