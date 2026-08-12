package com.binarybrains.slms.attendance.controller;

import com.binarybrains.slms.attendance.dto.AttendanceResponse;
import com.binarybrains.slms.attendance.dto.CreateAttendanceRequest;
import com.binarybrains.slms.attendance.service.AttendanceService;
import com.binarybrains.slms.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@Tag(name = "Attendance Management", description = "Employee attendance tracking")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    @Operation(summary = "Record attendance")
    public ResponseEntity<ApiResponse<AttendanceResponse>> recordAttendance(
            @Valid @RequestBody CreateAttendanceRequest request) {
        AttendanceResponse response = attendanceService.recordAttendance(request);
        return new ResponseEntity<>(ApiResponse.success("Attendance recorded", response), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get attendance by ID")
    public ResponseEntity<ApiResponse<AttendanceResponse>> getAttendance(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getAttendanceById(id)));
    }

    @GetMapping
    @Operation(summary = "Get all attendance records")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getAllAttendance() {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getAllAttendance()));
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get attendance by employee")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getByEmployee(@PathVariable String employeeId) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getAttendanceByEmployee(employeeId)));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get attendance by date")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getAttendanceByDate(date)));
    }

    @GetMapping("/range")
    @Operation(summary = "Get attendance by date range")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getAttendanceByDateRange(start, end)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete attendance record")
    public ResponseEntity<ApiResponse<Void>> deleteAttendance(@PathVariable String id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.ok(ApiResponse.success("Attendance deleted", null));
    }
}
