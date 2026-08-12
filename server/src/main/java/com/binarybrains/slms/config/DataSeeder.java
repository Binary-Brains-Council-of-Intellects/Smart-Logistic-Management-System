package com.binarybrains.slms.config;

import com.binarybrains.slms.attendance.model.Attendance;
import com.binarybrains.slms.attendance.model.AttendanceStatus;
import com.binarybrains.slms.attendance.repository.AttendanceRepository;
import com.binarybrains.slms.customer.model.Customer;
import com.binarybrains.slms.customer.repository.CustomerRepository;
import com.binarybrains.slms.employee.model.DeliveryDriver;
import com.binarybrains.slms.employee.model.Manager;
import com.binarybrains.slms.employee.model.WarehouseStaff;
import com.binarybrains.slms.employee.repository.EmployeeRepository;
import com.binarybrains.slms.inventory.model.NonPerishableProduct;
import com.binarybrains.slms.inventory.model.PerishableProduct;
import com.binarybrains.slms.inventory.model.ProductCategory;
import com.binarybrains.slms.inventory.repository.ProductRepository;
import com.binarybrains.slms.order.model.Order;
import com.binarybrains.slms.order.model.OrderItem;
import com.binarybrains.slms.order.model.OrderStatus;
import com.binarybrains.slms.order.repository.OrderRepository;
import com.binarybrains.slms.payroll.model.Payroll;
import com.binarybrains.slms.payroll.repository.PayrollRepository;
import com.binarybrains.slms.returns.model.ReturnReason;
import com.binarybrains.slms.returns.model.ReturnRequest;
import com.binarybrains.slms.returns.model.ReturnStatus;
import com.binarybrains.slms.returns.repository.ReturnRepository;
import com.binarybrains.slms.review.model.Review;
import com.binarybrains.slms.review.repository.ReviewRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Seed data for development profile.
 * Populates realistic sample products, employees, customers, orders, attendance, reviews, and returns.
 */
@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final AttendanceRepository attendanceRepository;
    private final PayrollRepository payrollRepository;
    private final ReviewRepository reviewRepository;
    private final ReturnRepository returnRepository;

    public DataSeeder(ProductRepository productRepository, EmployeeRepository employeeRepository,
                      CustomerRepository customerRepository, OrderRepository orderRepository,
                      AttendanceRepository attendanceRepository, PayrollRepository payrollRepository,
                      ReviewRepository reviewRepository, ReturnRepository returnRepository) {
        this.productRepository = productRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.attendanceRepository = attendanceRepository;
        this.payrollRepository = payrollRepository;
        this.reviewRepository = reviewRepository;
        this.returnRepository = returnRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            seedData();
        }
    }

    private void seedData() {
        System.out.println(">>> Seeding SLMS Development Data...");

        // 1. Seed Products
        PerishableProduct milk = new PerishableProduct(
                "Fresh Organic Milk 1L", "PER-MILK-001", "Pasteurized whole milk",
                ProductCategory.DAIRY, "BATCH-2026-A", LocalDate.now().minusDays(5),
                LocalDate.now().plusDays(10), 150, 1.20, 2.50, 4.0, true
        );

        PerishableProduct apples = new PerishableProduct(
                "Red Gala Apples 1kg", "PER-APPL-002", "Fresh crisp apples",
                ProductCategory.FOOD, "BATCH-2026-B", LocalDate.now().minusDays(10),
                LocalDate.now().plusDays(3), 80, 0.80, 1.99, 10.0, false
        );

        PerishableProduct expiredYogurt = new PerishableProduct(
                "Greek Yogurt 500g", "PER-YOG-003", "Natural probiotic yogurt",
                ProductCategory.DAIRY, "BATCH-2026-OLD", LocalDate.now().minusDays(30),
                LocalDate.now().minusDays(2), 20, 1.00, 2.20, 4.0, true
        );

        NonPerishableProduct laptop = new NonPerishableProduct(
                "Pro Logistics Scanner V2", "NON-SCAN-101", "Handheld barcode and QR scanner",
                ProductCategory.ELECTRONICS, "BATCH-E-99", LocalDate.now().minusMonths(2),
                50, 120.00, 249.99, 24, 0.8, true
        );

        NonPerishableProduct jacket = new NonPerishableProduct(
                "Reflective Warehouse Jacket", "NON-JAC-102", "High-visibility safety jacket",
                ProductCategory.CLOTHING, "BATCH-C-12", LocalDate.now().minusMonths(4),
                200, 15.00, 39.99, 12, 1.2, false
        );

        productRepository.saveAll(List.of(milk, apples, expiredYogurt, laptop, jacket));

        // 2. Seed Employees
        WarehouseStaff staff = new WarehouseStaff(
                "John Doe", "john.doe@slms.com", "+1-555-0101", "Logistics",
                3000.0, LocalDate.now().minusYears(2), "Aisle 4 - Cold Storage", "NIGHT", 12.5, 1.5
        );

        DeliveryDriver driver = new DeliveryDriver(
                "Alice Smith", "alice.smith@slms.com", "+1-555-0102", "Transport",
                2800.0, LocalDate.now().minusYears(1), "DL-987654321", "Refrigerated Truck", 45, 15.0
        );

        Manager manager = new Manager(
                "Robert Johnson", "robert.j@slms.com", "+1-555-0103", "Operations",
                6000.0, LocalDate.now().minusYears(4), "Inventory Operations", 15, 1200.0, 100.0
        );

        employeeRepository.saveAll(List.of(staff, driver, manager));

        // 3. Seed Customers
        Customer c1 = new Customer("Acme Supermarket", "orders@acme.com", "+1-555-0201", "123 Commerce St, Metro City", "WHOLESALE");
        Customer c2 = new Customer("Jane Retailer", "jane@retailer.org", "+1-555-0202", "456 High St, Suburbia", "REGULAR");
        customerRepository.saveAll(List.of(c1, c2));

        // 4. Seed Orders
        Order order1 = new Order();
        order1.setCustomerId(c1.getId());
        order1.setCustomerName(c1.getName());
        order1.setPricingStrategy("WHOLESALE");
        order1.setNotes("Urgent wholesale delivery");
        order1.addItem(new OrderItem(milk.getProductId(), milk.getName(), 50, 2.25));
        order1.addItem(new OrderItem(laptop.getProductId(), laptop.getName(), 2, 224.99));
        order1.setStatus(OrderStatus.DELIVERED);
        order1.calculateTotal();

        Order order2 = new Order();
        order2.setCustomerId(c2.getId());
        order2.setCustomerName(c2.getName());
        order2.setPricingStrategy("REGULAR");
        order2.addItem(new OrderItem(jacket.getProductId(), jacket.getName(), 5, 39.99));
        order2.setStatus(OrderStatus.CONFIRMED);
        order2.calculateTotal();

        orderRepository.saveAll(List.of(order1, order2));

        // 5. Seed Attendance
        Attendance att1 = new Attendance(staff.getId(), staff.getName(), LocalDate.now(), AttendanceStatus.PRESENT, LocalTime.of(8, 0), LocalTime.of(17, 0));
        Attendance att2 = new Attendance(driver.getId(), driver.getName(), LocalDate.now(), AttendanceStatus.PRESENT, LocalTime.of(8, 30), LocalTime.of(17, 30));
        attendanceRepository.saveAll(List.of(att1, att2));

        // 6. Seed Payroll
        Payroll p1 = new Payroll(
                staff.getId(), staff.getName(), staff.getEmployeeType(),
                LocalDate.now().getMonthValue(), LocalDate.now().getYear(),
                staff.getBaseSalary(), 0.0, 100.0, staff.calculatePayroll() - staff.getBaseSalary(),
                staff.calculatePayroll(), staff.calculatePayroll() - 100.0
        );
        payrollRepository.save(p1);

        // 7. Seed Reviews
        Review r1 = new Review(c1.getId(), c1.getName(), milk.getProductId(), milk.getName(), 5, "Excellent fresh quality and fast delivery!");
        Review r2 = new Review(c2.getId(), c2.getName(), jacket.getProductId(), jacket.getName(), 4, "Good jacket, fits well.");
        reviewRepository.saveAll(List.of(r1, r2));

        // 8. Seed Returns
        ReturnRequest ret1 = new ReturnRequest(
                order1.getId(), c1.getId(), c1.getName(),
                milk.getProductId(), milk.getName(), 2,
                ReturnReason.DAMAGED, "Two bottles cracked in transit"
        );
        returnRepository.save(ret1);

        System.out.println(">>> SLMS Development Data Seeded Successfully!");
    }
}
