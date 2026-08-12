package com.binarybrains.slms.attendance;

import com.binarybrains.slms.attendance.dto.AttendanceResponse;
import com.binarybrains.slms.attendance.dto.CreateAttendanceRequest;
import com.binarybrains.slms.attendance.model.Attendance;
import com.binarybrains.slms.attendance.model.AttendanceStatus;
import com.binarybrains.slms.attendance.repository.AttendanceRepository;
import com.binarybrains.slms.attendance.service.AttendanceService;
import com.binarybrains.slms.common.exception.DuplicateAttendanceException;
import com.binarybrains.slms.employee.model.WarehouseStaff;
import com.binarybrains.slms.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Attendance Service Unit Tests")
class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    private WarehouseStaff staff;

    @BeforeEach
    void setUp() {
        staff = new WarehouseStaff();
        staff.setId("EMP-1");
        staff.setName("Alice");
    }

    @Test
    @DisplayName("Should record attendance successfully when valid")
    void testRecordAttendanceSuccess() {
        CreateAttendanceRequest request = new CreateAttendanceRequest();
        request.setEmployeeId("EMP-1");
        request.setDate(LocalDate.now());
        request.setStatus(AttendanceStatus.PRESENT);
        request.setCheckIn(LocalTime.of(9, 0));
        request.setCheckOut(LocalTime.of(17, 0));

        when(employeeRepository.findById("EMP-1")).thenReturn(Optional.of(staff));
        when(attendanceRepository.existsByEmployeeIdAndDate("EMP-1", LocalDate.now())).thenReturn(false);
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(inv -> inv.getArgument(0));

        AttendanceResponse response = attendanceService.recordAttendance(request);
        assertNotNull(response);
    }

    @Test
    @DisplayName("Should throw DuplicateAttendanceException when attendance already recorded for date")
    void testRecordDuplicateAttendance() {
        CreateAttendanceRequest request = new CreateAttendanceRequest();
        request.setEmployeeId("EMP-1");
        request.setDate(LocalDate.now());

        when(employeeRepository.findById("EMP-1")).thenReturn(Optional.of(staff));
        when(attendanceRepository.existsByEmployeeIdAndDate("EMP-1", LocalDate.now())).thenReturn(true);

        assertThrows(DuplicateAttendanceException.class, () -> attendanceService.recordAttendance(request));
    }
}
