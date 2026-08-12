package com.binarybrains.slms.attendance.service;

import com.binarybrains.slms.attendance.dto.AttendanceResponse;
import com.binarybrains.slms.attendance.dto.CreateAttendanceRequest;
import com.binarybrains.slms.attendance.model.Attendance;
import com.binarybrains.slms.attendance.repository.AttendanceRepository;
import com.binarybrains.slms.common.exception.DuplicateAttendanceException;
import com.binarybrains.slms.common.exception.EmployeeNotFoundException;
import com.binarybrains.slms.employee.model.Employee;
import com.binarybrains.slms.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             EmployeeRepository employeeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    public AttendanceResponse recordAttendance(CreateAttendanceRequest request) {
        // Validate employee exists
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException(request.getEmployeeId()));

        // Prevent duplicate attendance for same employee/date
        if (attendanceRepository.existsByEmployeeIdAndDate(request.getEmployeeId(), request.getDate())) {
            throw new DuplicateAttendanceException(request.getEmployeeId(), request.getDate().toString());
        }

        Attendance attendance = new Attendance(
                request.getEmployeeId(),
                employee.getName(),
                request.getDate(),
                request.getStatus(),
                request.getCheckIn(),
                request.getCheckOut()
        );
        attendance.setNotes(request.getNotes());

        Attendance saved = attendanceRepository.save(attendance);
        return AttendanceResponse.fromAttendance(saved);
    }

    public AttendanceResponse getAttendanceById(String id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance record not found: " + id));
        return AttendanceResponse.fromAttendance(attendance);
    }

    public List<AttendanceResponse> getAttendanceByEmployee(String employeeId) {
        return attendanceRepository.findByEmployeeId(employeeId).stream()
                .map(AttendanceResponse::fromAttendance)
                .collect(Collectors.toList());
    }

    public List<AttendanceResponse> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDate(date).stream()
                .map(AttendanceResponse::fromAttendance)
                .collect(Collectors.toList());
    }

    public List<AttendanceResponse> getAttendanceByEmployeeAndDateRange(
            String employeeId, LocalDate start, LocalDate end) {
        return attendanceRepository.findByEmployeeIdAndDateBetween(employeeId, start, end).stream()
                .map(AttendanceResponse::fromAttendance)
                .collect(Collectors.toList());
    }

    public List<AttendanceResponse> getAttendanceByDateRange(LocalDate start, LocalDate end) {
        return attendanceRepository.findByDateBetween(start, end).stream()
                .map(AttendanceResponse::fromAttendance)
                .collect(Collectors.toList());
    }

    public List<AttendanceResponse> getAllAttendance() {
        return attendanceRepository.findAll().stream()
                .map(AttendanceResponse::fromAttendance)
                .collect(Collectors.toList());
    }

    public void deleteAttendance(String id) {
        attendanceRepository.deleteById(id);
    }
}
