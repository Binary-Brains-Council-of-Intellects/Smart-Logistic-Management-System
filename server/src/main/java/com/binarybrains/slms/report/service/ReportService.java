package com.binarybrains.slms.report.service;

import com.binarybrains.slms.customer.repository.CustomerRepository;
import com.binarybrains.slms.employee.model.EmployeeType;
import com.binarybrains.slms.employee.repository.EmployeeRepository;
import com.binarybrains.slms.inventory.model.Product;
import com.binarybrains.slms.inventory.model.ProductType;
import com.binarybrains.slms.inventory.repository.ProductRepository;
import com.binarybrains.slms.order.model.Order;
import com.binarybrains.slms.order.model.OrderStatus;
import com.binarybrains.slms.order.repository.OrderRepository;
import com.binarybrains.slms.report.dto.*;
import com.binarybrains.slms.returns.model.ReturnStatus;
import com.binarybrains.slms.returns.repository.ReturnRepository;
import com.binarybrains.slms.review.model.Review;
import com.binarybrains.slms.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Cross-module reporting service.
 * Aggregates data from all repositories to produce comprehensive reports.
 */
@Service
public class ReportService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final ReviewRepository reviewRepository;
    private final ReturnRepository returnRepository;

    public ReportService(ProductRepository productRepository, OrderRepository orderRepository,
                         EmployeeRepository employeeRepository, CustomerRepository customerRepository,
                         ReviewRepository reviewRepository, ReturnRepository returnRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.reviewRepository = reviewRepository;
        this.returnRepository = returnRepository;
    }

    public DashboardSummaryDTO getDashboardSummary() {
        DashboardSummaryDTO dto = new DashboardSummaryDTO();
        dto.setTotalProducts(productRepository.count());
        dto.setTotalOrders(orderRepository.count());
        dto.setTotalCustomers(customerRepository.countByActiveTrue());
        dto.setTotalEmployees(employeeRepository.countByActiveTrue());

        List<Order> allOrders = orderRepository.findAll();
        dto.setTotalRevenue(allOrders.stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .mapToDouble(Order::getTotalAmount).sum());

        dto.setPendingOrders(orderRepository.countByStatus(OrderStatus.PENDING));
        dto.setCompletedOrders(orderRepository.countByStatus(OrderStatus.DELIVERED));
        dto.setCancelledOrders(orderRepository.countByStatus(OrderStatus.CANCELLED));
        dto.setExpiredProducts(productRepository.findExpiredProducts(LocalDate.now()).size());
        dto.setLowStockProducts(productRepository.findLowStockProducts(10).size());
        dto.setPendingReturns(returnRepository.countByStatus(ReturnStatus.REQUESTED));

        List<Review> allReviews = reviewRepository.findAll();
        dto.setAverageProductRating(allReviews.isEmpty() ? 0.0 :
                allReviews.stream().mapToInt(Review::getRating).average().orElse(0.0));

        return dto;
    }

    public SalesReportDTO getSalesReport() {
        SalesReportDTO dto = new SalesReportDTO();
        List<Order> allOrders = orderRepository.findAll();
        dto.setTotalOrders(allOrders.size());
        dto.setConfirmedOrders(orderRepository.countByStatus(OrderStatus.CONFIRMED));
        dto.setDeliveredOrders(orderRepository.countByStatus(OrderStatus.DELIVERED));
        dto.setCancelledOrders(orderRepository.countByStatus(OrderStatus.CANCELLED));

        double revenue = allOrders.stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .mapToDouble(Order::getTotalAmount).sum();
        dto.setTotalRevenue(revenue);

        long nonCancelled = allOrders.stream().filter(o -> o.getStatus() != OrderStatus.CANCELLED).count();
        dto.setAverageOrderValue(nonCancelled > 0 ? revenue / nonCancelled : 0.0);

        return dto;
    }

    public InventoryReportDTO getInventoryReport() {
        InventoryReportDTO dto = new InventoryReportDTO();
        List<Product> allProducts = productRepository.findAll();
        dto.setTotalProducts(allProducts.size());
        dto.setActiveProducts(productRepository.findByActiveTrue().size());
        dto.setTotalStock(allProducts.stream().mapToLong(Product::getAvailableQuantity).sum());
        dto.setLowStockProducts(productRepository.findLowStockProducts(10).size());
        dto.setExpiredProducts(productRepository.findExpiredProducts(LocalDate.now()).size());
        dto.setNearExpiryProducts(productRepository.findNearExpiryProducts(
                LocalDate.now(), LocalDate.now().plusDays(30)).size());
        dto.setPerishableProducts(productRepository.findByProductType(ProductType.PERISHABLE).size());
        dto.setNonPerishableProducts(productRepository.findByProductType(ProductType.NON_PERISHABLE).size());
        return dto;
    }

    public WorkforceReportDTO getWorkforceReport() {
        WorkforceReportDTO dto = new WorkforceReportDTO();
        dto.setTotalEmployees(employeeRepository.count());
        dto.setActiveEmployees(employeeRepository.countByActiveTrue());
        dto.setWarehouseStaffCount(employeeRepository.countByEmployeeType(EmployeeType.WAREHOUSE_STAFF));
        dto.setDeliveryDriverCount(employeeRepository.countByEmployeeType(EmployeeType.DELIVERY_DRIVER));
        dto.setManagerCount(employeeRepository.countByEmployeeType(EmployeeType.MANAGER));

        var activeEmployees = employeeRepository.findByActiveTrue();
        double totalPayroll = activeEmployees.stream()
                .mapToDouble(e -> e.calculatePayroll()) // POLYMORPHISM in reporting
                .sum();
        dto.setTotalPayroll(totalPayroll);
        dto.setAverageSalary(activeEmployees.isEmpty() ? 0.0 : totalPayroll / activeEmployees.size());

        return dto;
    }
}
