package com.binarybrains.slms.attendance.dto;

import com.binarybrains.slms.attendance.model.Attendance;
import com.binarybrains.slms.attendance.model.AttendanceStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class AttendanceResponse {

    private String id;
    private String employeeId;
    private String employeeName;
    private LocalDate date;
    private AttendanceStatus status;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private double hoursWorked;
    private String notes;

    public static AttendanceResponse fromAttendance(Attendance attendance) {
        AttendanceResponse response = new AttendanceResponse();
        response.id = attendance.getId();
        response.employeeId = attendance.getEmployeeId();
        response.employeeName = attendance.getEmployeeName();
        response.date = attendance.getDate();
        response.status = attendance.getStatus();
        response.checkIn = attendance.getCheckIn();
        response.checkOut = attendance.getCheckOut();
        response.hoursWorked = attendance.calculateHoursWorked();
        response.notes = attendance.getNotes();
        return response;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) { this.status = status; }

    public LocalTime getCheckIn() { return checkIn; }
    public void setCheckIn(LocalTime checkIn) { this.checkIn = checkIn; }

    public LocalTime getCheckOut() { return checkOut; }
    public void setCheckOut(LocalTime checkOut) { this.checkOut = checkOut; }

    public double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(double hoursWorked) { this.hoursWorked = hoursWorked; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
