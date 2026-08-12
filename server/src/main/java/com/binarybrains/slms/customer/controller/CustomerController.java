package com.binarybrains.slms.customer.controller;

import com.binarybrains.slms.common.response.ApiResponse;
import com.binarybrains.slms.customer.dto.CreateCustomerRequest;
import com.binarybrains.slms.customer.dto.CustomerResponse;
import com.binarybrains.slms.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer Management", description = "Customer CRUD endpoints")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @Operation(summary = "Create a new customer")
    public ResponseEntity<ApiResponse<CustomerResponse>> create(@Valid @RequestBody CreateCustomerRequest req) {
        return new ResponseEntity<>(ApiResponse.success("Customer created", customerService.createCustomer(req)), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(customerService.getCustomerById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(customerService.getAllCustomers()));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getActive() {
        return ResponseEntity.ok(ApiResponse.success(customerService.getActiveCustomers()));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success(customerService.searchCustomers(keyword)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> update(@PathVariable String id, @Valid @RequestBody CreateCustomerRequest req) {
        return ResponseEntity.ok(ApiResponse.success("Customer updated", customerService.updateCustomer(id, req)));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable String id) {
        customerService.deactivateCustomer(id); return ResponseEntity.ok(ApiResponse.success("Deactivated", null));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activate(@PathVariable String id) {
        customerService.activateCustomer(id); return ResponseEntity.ok(ApiResponse.success("Activated", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        customerService.deleteCustomer(id); return ResponseEntity.ok(ApiResponse.success("Deleted", null));
    }
}
