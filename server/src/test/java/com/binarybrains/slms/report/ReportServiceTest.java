package com.binarybrains.slms.report;

import com.binarybrains.slms.customer.repository.CustomerRepository;
import com.binarybrains.slms.employee.repository.EmployeeRepository;
import com.binarybrains.slms.inventory.repository.ProductRepository;
import com.binarybrains.slms.order.model.Order;
import com.binarybrains.slms.order.model.OrderStatus;
import com.binarybrains.slms.order.repository.OrderRepository;
import com.binarybrains.slms.report.dto.DashboardSummaryDTO;
import com.binarybrains.slms.report.service.ReportService;
import com.binarybrains.slms.returns.repository.ReturnRepository;
import com.binarybrains.slms.review.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Report Service Unit Tests")
class ReportServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReturnRepository returnRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    @DisplayName("Should generate dashboard summary correctly")
    void testGetDashboardSummary() {
        Order o1 = new Order(); o1.setTotalAmount(500.0); o1.setStatus(OrderStatus.DELIVERED);
        Order o2 = new Order(); o2.setTotalAmount(300.0); o2.setStatus(OrderStatus.CONFIRMED);

        when(productRepository.count()).thenReturn(10L);
        when(orderRepository.count()).thenReturn(2L);
        when(customerRepository.countByActiveTrue()).thenReturn(5L);
        when(employeeRepository.countByActiveTrue()).thenReturn(4L);
        when(orderRepository.findAll()).thenReturn(List.of(o1, o2));
        when(reviewRepository.findAll()).thenReturn(Collections.emptyList());

        DashboardSummaryDTO summary = reportService.getDashboardSummary();

        assertNotNull(summary);
        assertEquals(10L, summary.getTotalProducts());
        assertEquals(2L, summary.getTotalOrders());
        assertEquals(800.0, summary.getTotalRevenue());
    }
}
