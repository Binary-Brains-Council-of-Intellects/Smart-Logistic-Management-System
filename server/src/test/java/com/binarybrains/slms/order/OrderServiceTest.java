package com.binarybrains.slms.order;

import com.binarybrains.slms.common.exception.ExpiredProductException;
import com.binarybrains.slms.common.exception.InsufficientStockException;
import com.binarybrains.slms.customer.model.Customer;
import com.binarybrains.slms.customer.repository.CustomerRepository;
import com.binarybrains.slms.inventory.model.NonPerishableProduct;
import com.binarybrains.slms.inventory.model.PerishableProduct;
import com.binarybrains.slms.inventory.model.ProductCategory;
import com.binarybrains.slms.inventory.repository.ProductRepository;
import com.binarybrains.slms.inventory.strategy.PricingContext;
import com.binarybrains.slms.inventory.strategy.RegularPricingStrategy;
import com.binarybrains.slms.inventory.strategy.WholesalePricingStrategy;
import com.binarybrains.slms.order.dto.CreateOrderRequest;
import com.binarybrains.slms.order.dto.OrderResponse;
import com.binarybrains.slms.order.model.Order;
import com.binarybrains.slms.order.model.OrderStatus;
import com.binarybrains.slms.order.repository.OrderRepository;
import com.binarybrains.slms.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Order Service Integration & Unit Tests")
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    private PricingContext pricingContext = new PricingContext(
            List.of(new RegularPricingStrategy(), new WholesalePricingStrategy())
    );

    private OrderService orderService;

    private Customer customer;
    private NonPerishableProduct validProduct;
    private PerishableProduct expiredProduct;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, productRepository, customerRepository, pricingContext);
        customer = new Customer("Test Customer", "cust@test.com", "123", "Addr", "REGULAR");
        customer.setId("CUST-1");

        validProduct = new NonPerishableProduct("TV", "SKU-TV", "Smart TV", ProductCategory.ELECTRONICS,
                "B1", LocalDate.now(), 20, 300.0, 500.0, 12, 10.0, true);
        validProduct.setProductId("PROD-TV");

        expiredProduct = new PerishableProduct("Milk", "SKU-MILK", "Old milk", ProductCategory.DAIRY,
                "B2", LocalDate.now().minusDays(20), LocalDate.now().minusDays(2),
                50, 1.0, 2.0, 4.0, true);
        expiredProduct.setProductId("PROD-MILK");
    }

    @Test
    @DisplayName("Should create order successfully and deduct stock")
    void testCreateOrderSuccess() {
        when(customerRepository.findById("CUST-1")).thenReturn(Optional.of(customer));
        when(productRepository.findById("PROD-TV")).thenReturn(Optional.of(validProduct));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId("CUST-1");
        request.setPricingStrategy("REGULAR");

        CreateOrderRequest.OrderItemRequest itemReq = new CreateOrderRequest.OrderItemRequest();
        itemReq.setProductId("PROD-TV");
        itemReq.setQuantity(2);
        request.setItems(List.of(itemReq));

        OrderResponse response = orderService.createOrder(request);

        assertNotNull(response);
        assertEquals(1000.0, response.getTotalAmount());
        assertEquals(18, validProduct.getAvailableQuantity()); // Stock deducted
        assertEquals(OrderStatus.CONFIRMED, response.getStatus());

        verify(productRepository).saveAll(any());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Should reject ordering an expired product")
    void testCreateOrderWithExpiredProduct() {
        when(customerRepository.findById("CUST-1")).thenReturn(Optional.of(customer));
        when(productRepository.findById("PROD-MILK")).thenReturn(Optional.of(expiredProduct));

        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId("CUST-1");

        CreateOrderRequest.OrderItemRequest itemReq = new CreateOrderRequest.OrderItemRequest();
        itemReq.setProductId("PROD-MILK");
        itemReq.setQuantity(1);
        request.setItems(List.of(itemReq));

        assertThrows(ExpiredProductException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should reject order if insufficient stock")
    void testCreateOrderInsufficientStock() {
        when(customerRepository.findById("CUST-1")).thenReturn(Optional.of(customer));
        when(productRepository.findById("PROD-TV")).thenReturn(Optional.of(validProduct));

        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId("CUST-1");

        CreateOrderRequest.OrderItemRequest itemReq = new CreateOrderRequest.OrderItemRequest();
        itemReq.setProductId("PROD-TV");
        itemReq.setQuantity(50); // only 20 available
        request.setItems(List.of(itemReq));

        assertThrows(InsufficientStockException.class, () -> orderService.createOrder(request));
    }
}
