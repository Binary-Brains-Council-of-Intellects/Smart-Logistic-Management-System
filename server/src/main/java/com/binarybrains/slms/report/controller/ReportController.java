package com.binarybrains.slms.report.controller;

import com.binarybrains.slms.common.response.ApiResponse;
import com.binarybrains.slms.report.dto.*;
import com.binarybrains.slms.report.service.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports & Analytics", description = "Cross-module reporting and dashboard")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardSummaryDTO>> getDashboard() {
        return ResponseEntity.ok(ApiResponse.success(reportService.getDashboardSummary()));
    }

    @GetMapping("/sales")
    public ResponseEntity<ApiResponse<SalesReportDTO>> getSalesReport() {
        return ResponseEntity.ok(ApiResponse.success(reportService.getSalesReport()));
    }

    @GetMapping("/inventory")
    public ResponseEntity<ApiResponse<InventoryReportDTO>> getInventoryReport() {
        return ResponseEntity.ok(ApiResponse.success(reportService.getInventoryReport()));
    }

    @GetMapping("/workforce")
    public ResponseEntity<ApiResponse<WorkforceReportDTO>> getWorkforceReport() {
        return ResponseEntity.ok(ApiResponse.success(reportService.getWorkforceReport()));
    }
}
