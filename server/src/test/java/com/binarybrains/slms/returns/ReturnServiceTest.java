package com.binarybrains.slms.returns;

import com.binarybrains.slms.customer.model.Customer;
import com.binarybrains.slms.customer.repository.CustomerRepository;
import com.binarybrains.slms.inventory.model.NonPerishableProduct;
import com.binarybrains.slms.inventory.model.ProductCategory;
import com.binarybrains.slms.inventory.repository.ProductRepository;
import com.binarybrains.slms.order.model.Order;
import com.binarybrains.slms.order.model.OrderItem;
import com.binarybrains.slms.order.repository.OrderRepository;
import com.binarybrains.slms.returns.dto.CreateReturnRequest;
import com.binarybrains.slms.returns.dto.ReturnResponse;
import com.binarybrains.slms.returns.model.ReturnReason;
import com.binarybrains.slms.returns.model.ReturnRequest;
import com.binarybrains.slms.returns.model.ReturnStatus;
import com.binarybrains.slms.returns.repository.ReturnRepository;
import com.binarybrains.slms.returns.service.ReturnService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Return Service Unit Tests")
class ReturnServiceTest {

    @Mock
    private ReturnRepository returnRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ReturnService returnService;

    private Order order;
    private Customer customer;
    private NonPerishableProduct product;

    @BeforeEach
    void setUp() {
        customer = new Customer("Jane", "jane@test.com", "123", "Addr", "REGULAR");
        customer.setId("CUST-1");

        product = new NonPerishableProduct("Shirt", "SKU-S", "Blue shirt", ProductCategory.CLOTHING,
                "B1", LocalDate.now(), 100, 10.0, 25.0, 0, 0.2, false);
        product.setProductId("PROD-1");

        order = new Order();
        order.setId("ORD-1");
        order.setCustomerId("CUST-1");
        order.addItem(new OrderItem("PROD-1", "Shirt", 5, 25.0));
    }

    @Test
    @DisplayName("Should create return request successfully")
    void testCreateReturnRequest() {
        CreateReturnRequest request = new CreateReturnRequest();
        request.setOrderId("ORD-1");
        request.setCustomerId("CUST-1");
        request.setProductId("PROD-1");
        request.setQuantity(2);
        request.setReason(ReturnReason.WRONG_ITEM);

        when(orderRepository.findById("ORD-1")).thenReturn(Optional.of(order));
        when(customerRepository.findById("CUST-1")).thenReturn(Optional.of(customer));
        when(productRepository.findById("PROD-1")).thenReturn(Optional.of(product));
        when(returnRepository.save(any(ReturnRequest.class))).thenAnswer(inv -> inv.getArgument(0));

        ReturnResponse response = returnService.createReturnRequest(request);

        assertNotNull(response);
        assertEquals(ReturnStatus.REQUESTED, response.getStatus());
        verify(returnRepository).save(any(ReturnRequest.class));
    }

    @Test
    @DisplayName("Should NOT restore stock for DAMAGED product when return completed")
    void testCompleteReturnDamagedNoStockRestoration() {
        ReturnRequest damagedReturn = new ReturnRequest("ORD-1", "CUST-1", "Jane", "PROD-1", "Shirt", 2, ReturnReason.DAMAGED, "Ripped");
        damagedReturn.setStatus(ReturnStatus.APPROVED);

        when(returnRepository.findById("RET-1")).thenReturn(Optional.of(damagedReturn));
        when(returnRepository.save(any(ReturnRequest.class))).thenAnswer(inv -> inv.getArgument(0));

        ReturnResponse response = returnService.completeReturn("RET-1");

        assertEquals(ReturnStatus.COMPLETED, response.getStatus());
        verify(productRepository, never()).findById(any()); // Product stock not touched for damaged item
    }

    @Test
    @DisplayName("Should restore stock for WRONG_ITEM product when return completed")
    void testCompleteReturnWrongItemRestoresStock() {
        ReturnRequest wrongItemReturn = new ReturnRequest("ORD-1", "CUST-1", "Jane", "PROD-1", "Shirt", 2, ReturnReason.WRONG_ITEM, "Wrong size");
        wrongItemReturn.setStatus(ReturnStatus.APPROVED);

        when(returnRepository.findById("RET-2")).thenReturn(Optional.of(wrongItemReturn));
        when(productRepository.findById("PROD-1")).thenReturn(Optional.of(product));
        when(returnRepository.save(any(ReturnRequest.class))).thenAnswer(inv -> inv.getArgument(0));

        ReturnResponse response = returnService.completeReturn("RET-2");

        assertEquals(ReturnStatus.COMPLETED, response.getStatus());
        assertEquals(102, product.getAvailableQuantity()); // 100 + 2 restored
        verify(productRepository).save(product);
    }
}
