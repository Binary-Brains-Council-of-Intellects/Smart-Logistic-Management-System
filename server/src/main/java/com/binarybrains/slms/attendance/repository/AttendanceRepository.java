package com.binarybrains.slms.attendance.repository;

import com.binarybrains.slms.attendance.model.Attendance;
import com.binarybrains.slms.attendance.model.AttendanceStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends MongoRepository<Attendance, String> {

    List<Attendance> findByEmployeeId(String employeeId);

    List<Attendance> findByDate(LocalDate date);

    List<Attendance> findByEmployeeIdAndDateBetween(String employeeId, LocalDate start, LocalDate end);

    List<Attendance> findByDateBetween(LocalDate start, LocalDate end);

    boolean existsByEmployeeIdAndDate(String employeeId, LocalDate date);

    long countByEmployeeIdAndStatusAndDateBetween(String employeeId, AttendanceStatus status,
                                                   LocalDate start, LocalDate end);

    long countByDateAndStatus(LocalDate date, AttendanceStatus status);
}
