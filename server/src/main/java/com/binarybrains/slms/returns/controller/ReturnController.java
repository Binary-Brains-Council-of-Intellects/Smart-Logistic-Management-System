package com.binarybrains.slms.returns.controller;

import com.binarybrains.slms.common.response.ApiResponse;
import com.binarybrains.slms.returns.dto.CreateReturnRequest;
import com.binarybrains.slms.returns.dto.ReturnResponse;
import com.binarybrains.slms.returns.model.ReturnStatus;
import com.binarybrains.slms.returns.service.ReturnService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/returns")
@Tag(name = "Return Management", description = "Product return and exchange handling")
public class ReturnController {

    private final ReturnService returnService;

    public ReturnController(ReturnService returnService) {
        this.returnService = returnService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReturnResponse>> create(@Valid @RequestBody CreateReturnRequest req) {
        return new ResponseEntity<>(ApiResponse.success("Return requested", returnService.createReturnRequest(req)), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReturnResponse>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(returnService.getReturnById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReturnResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(returnService.getAllReturns()));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<ReturnResponse>>> getByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(ApiResponse.success(returnService.getReturnsByCustomer(customerId)));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<ReturnResponse>>> getByOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(ApiResponse.success(returnService.getReturnsByOrder(orderId)));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<ReturnResponse>>> getByStatus(@PathVariable ReturnStatus status) {
        return ResponseEntity.ok(ApiResponse.success(returnService.getReturnsByStatus(status)));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<ReturnResponse>> approve(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success("Return approved", returnService.approveReturn(id)));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<ReturnResponse>> reject(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success("Return rejected", returnService.rejectReturn(id)));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<ReturnResponse>> complete(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success("Return completed", returnService.completeReturn(id)));
    }
}
