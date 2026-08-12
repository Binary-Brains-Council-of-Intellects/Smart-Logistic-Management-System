package com.binarybrains.slms.model;

import com.binarybrains.slms.employee.model.DeliveryDriver;
import com.binarybrains.slms.employee.model.Employee;
import com.binarybrains.slms.employee.model.Manager;
import com.binarybrains.slms.employee.model.WarehouseStaff;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Employee Polymorphic Payroll Unit Tests")
class EmployeeModelTest {

    @Test
    @DisplayName("Should verify polymorphic payroll calculation across employee subclasses")
    void testPolymorphicPayrollCalculation() {
        WarehouseStaff staff = new WarehouseStaff(
                "Staff 1", "staff1@slms.com", "123", "Warehouse",
                3200.0, LocalDate.now(), "Sec-A", "NIGHT", 10.0, 1.5
        );
        // Base: 3200, Hourly: 3200/160 = 20, Overtime: 10 * 20 * 1.5 = 300 -> Total: 3500

        DeliveryDriver driver = new DeliveryDriver(
                "Driver 1", "driver1@slms.com", "456", "Delivery",
                2500.0, LocalDate.now(), "LIC-100", "Van", 20, 25.0
        );
        // Base: 2500, Delivery bonus: 20 * 25 = 500 -> Total: 3000

        Manager manager = new Manager(
                "Manager 1", "mgr1@slms.com", "789", "Management",
                5000.0, LocalDate.now(), "Operations", 10, 1000.0, 100.0
        );
        // Base: 5000, Management bonus: 1000, Team bonus: 10 * 100 = 1000 -> Total: 7000

        assertEquals(3500.0, staff.calculatePayroll(), 0.01);
        assertEquals(3000.0, driver.calculatePayroll(), 0.01);
        assertEquals(7000.0, manager.calculatePayroll(), 0.01);

        // Test Runtime Polymorphism via List<Employee>
        List<Employee> employees = List.of(staff, driver, manager);
        double totalPayroll = 0.0;
        for (Employee emp : employees) {
            totalPayroll += emp.calculatePayroll(); // POLYMORPHIC DISPATCH
        }

        assertEquals(13500.0, totalPayroll, 0.01);
    }
}
