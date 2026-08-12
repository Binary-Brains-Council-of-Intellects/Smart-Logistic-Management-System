package com.binarybrains.slms.employee.controller;

import com.binarybrains.slms.common.response.ApiResponse;
import com.binarybrains.slms.employee.dto.*;
import com.binarybrains.slms.employee.model.EmployeeType;
import com.binarybrains.slms.employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee Management", description = "Employee CRUD and payroll endpoints")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @Operation(summary = "Create a new employee")
    public ResponseEntity<ApiResponse<EmployeeResponse>> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request) {
        EmployeeResponse response = employeeService.createEmployee(request);
        return new ResponseEntity<>(ApiResponse.success("Employee created successfully", response),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployee(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(employeeService.getEmployeeById(id)));
    }

    @GetMapping
    @Operation(summary = "Get all employees")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getAllEmployees() {
        return ResponseEntity.ok(ApiResponse.success(employeeService.getAllEmployees()));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active employees")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getActiveEmployees() {
        return ResponseEntity.ok(ApiResponse.success(employeeService.getActiveEmployees()));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Filter employees by type")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getByType(@PathVariable EmployeeType type) {
        return ResponseEntity.ok(ApiResponse.success(employeeService.getEmployeesByType(type)));
    }

    @GetMapping("/search")
    @Operation(summary = "Search employees by name")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> searchEmployees(@RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success(employeeService.searchEmployees(keyword)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an employee")
    public ResponseEntity<ApiResponse<EmployeeResponse>> updateEmployee(
            @PathVariable String id, @Valid @RequestBody UpdateEmployeeRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Employee updated", employeeService.updateEmployee(id, request)));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate an employee")
    public ResponseEntity<ApiResponse<Void>> deactivateEmployee(@PathVariable String id) {
        employeeService.deactivateEmployee(id);
        return ResponseEntity.ok(ApiResponse.success("Employee deactivated", null));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate an employee")
    public ResponseEntity<ApiResponse<Void>> activateEmployee(@PathVariable String id) {
        employeeService.activateEmployee(id);
        return ResponseEntity.ok(ApiResponse.success("Employee activated", null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an employee")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable String id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.success("Employee deleted", null));
    }
}
