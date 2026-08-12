package com.binarybrains.slms.payroll.controller;

import com.binarybrains.slms.common.response.ApiResponse;
import com.binarybrains.slms.payroll.dto.GeneratePayrollRequest;
import com.binarybrains.slms.payroll.dto.PayrollResponse;
import com.binarybrains.slms.payroll.service.PayrollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@Tag(name = "Payroll Management", description = "Automated payroll generation and history")
public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @PostMapping
    @Operation(summary = "Generate payroll for an employee", description = "Uses polymorphic calculatePayroll()")
    public ResponseEntity<ApiResponse<PayrollResponse>> generatePayroll(
            @Valid @RequestBody GeneratePayrollRequest request) {
        PayrollResponse response = payrollService.generatePayroll(request);
        return new ResponseEntity<>(ApiResponse.success("Payroll generated", response), HttpStatus.CREATED);
    }

    @PostMapping("/generate-all")
    @Operation(summary = "Generate payroll for all active employees")
    public ResponseEntity<ApiResponse<List<PayrollResponse>>> generatePayrollForAll(
            @RequestParam int month, @RequestParam int year) {
        List<PayrollResponse> responses = payrollService.generatePayrollForAll(month, year);
        return new ResponseEntity<>(ApiResponse.success("Payroll generated for all employees", responses), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payroll record by ID")
    public ResponseEntity<ApiResponse<PayrollResponse>> getPayroll(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(payrollService.getPayrollById(id)));
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get payroll history for an employee")
    public ResponseEntity<ApiResponse<List<PayrollResponse>>> getByEmployee(@PathVariable String employeeId) {
        return ResponseEntity.ok(ApiResponse.success(payrollService.getPayrollByEmployee(employeeId)));
    }

    @GetMapping("/period")
    @Operation(summary = "Get payroll by month/year")
    public ResponseEntity<ApiResponse<List<PayrollResponse>>> getByPeriod(
            @RequestParam int month, @RequestParam int year) {
        return ResponseEntity.ok(ApiResponse.success(payrollService.getPayrollByPeriod(month, year)));
    }

    @GetMapping
    @Operation(summary = "Get all payroll records")
    public ResponseEntity<ApiResponse<List<PayrollResponse>>> getAllPayroll() {
        return ResponseEntity.ok(ApiResponse.success(payrollService.getAllPayroll()));
    }
}
