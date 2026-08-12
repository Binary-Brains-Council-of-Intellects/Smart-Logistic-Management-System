package com.binarybrains.slms.employee.service;

import com.binarybrains.slms.employee.dto.CreateEmployeeRequest;
import com.binarybrains.slms.employee.dto.EmployeeResponse;
import com.binarybrains.slms.employee.dto.UpdateEmployeeRequest;
import com.binarybrains.slms.employee.model.Employee;
import com.binarybrains.slms.employee.model.EmployeeType;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(CreateEmployeeRequest request);

    EmployeeResponse getEmployeeById(String id);

    Employee getEmployeeEntityById(String id);

    List<EmployeeResponse> getAllEmployees();

    List<EmployeeResponse> getActiveEmployees();

    List<EmployeeResponse> getEmployeesByType(EmployeeType type);

    List<EmployeeResponse> searchEmployees(String keyword);

    EmployeeResponse updateEmployee(String id, UpdateEmployeeRequest request);

    void deactivateEmployee(String id);

    void activateEmployee(String id);

    void deleteEmployee(String id);
}
