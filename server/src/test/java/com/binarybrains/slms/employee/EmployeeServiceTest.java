package com.binarybrains.slms.employee;

import com.binarybrains.slms.employee.dto.CreateEmployeeRequest;
import com.binarybrains.slms.employee.dto.EmployeeResponse;
import com.binarybrains.slms.employee.factory.EmployeeFactory;
import com.binarybrains.slms.employee.model.Employee;
import com.binarybrains.slms.employee.model.EmployeeType;
import com.binarybrains.slms.employee.model.WarehouseStaff;
import com.binarybrains.slms.employee.repository.EmployeeRepository;
import com.binarybrains.slms.employee.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Employee Service Mockito Unit Tests")
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeFactory employeeFactory = new EmployeeFactory();

    private EmployeeServiceImpl employeeService;

    private CreateEmployeeRequest request;
    private WarehouseStaff staff;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeServiceImpl(employeeRepository, employeeFactory);
        request = new CreateEmployeeRequest();
        request.setName("John Worker");
        request.setEmail("john@slms.com");
        request.setPhone("1234567890");
        request.setDepartment("Warehouse");
        request.setBaseSalary(3000.0);
        request.setEmployeeType(EmployeeType.WAREHOUSE_STAFF);

        staff = new WarehouseStaff(
                "John Worker", "john@slms.com", "1234567890", "Warehouse",
                3000.0, LocalDate.now(), "Aisle 1", "DAY", 0.0, 1.5
        );
        staff.setId("EMP-1");
    }

    @Test
    @DisplayName("Should create employee successfully")
    void testCreateEmployee() {
        when(employeeRepository.existsByEmail("john@slms.com")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(staff);

        EmployeeResponse response = employeeService.createEmployee(request);

        assertNotNull(response);
        assertEquals("EMP-1", response.getId());
        assertEquals("John Worker", response.getName());
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should retrieve employee by ID with correct polymorphic payroll")
    void testGetEmployeeById() {
        when(employeeRepository.findById("EMP-1")).thenReturn(Optional.of(staff));

        EmployeeResponse response = employeeService.getEmployeeById("EMP-1");

        assertNotNull(response);
        assertEquals(3000.0, response.getCalculatedPayroll());
    }
}
