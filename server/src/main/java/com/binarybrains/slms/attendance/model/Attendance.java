package com.binarybrains.slms.attendance.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Attendance record for an employee.
 * Compound index on (employeeId, date) prevents duplicates.
 */
@Document(collection = "attendance")
@CompoundIndex(name = "employee_date_idx", def = "{'employee_id': 1, 'date': 1}", unique = true)
public class Attendance {

    @Id
    private String id;

    @Field("employee_id")
    private String employeeId;

    @Field("employee_name")
    private String employeeName;

    @Field("date")
    private LocalDate date;

    @Field("status")
    private AttendanceStatus status;

    @Field("check_in")
    private LocalTime checkIn;

    @Field("check_out")
    private LocalTime checkOut;

    @Field("notes")
    private String notes;

    public Attendance() {}

    public Attendance(String employeeId, String employeeName, LocalDate date,
                      AttendanceStatus status, LocalTime checkIn, LocalTime checkOut) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.date = date;
        this.status = status;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    /**
     * Calculates hours worked based on check-in and check-out.
     */
    public double calculateHoursWorked() {
        if (checkIn == null || checkOut == null) {
            return 0.0;
        }
        long minutes = java.time.Duration.between(checkIn, checkOut).toMinutes();
        return minutes / 60.0;
    }

    // --- Getters and Setters ---

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

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
