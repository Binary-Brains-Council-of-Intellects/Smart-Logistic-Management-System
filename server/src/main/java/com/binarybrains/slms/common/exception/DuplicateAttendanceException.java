package com.binarybrains.slms.common.exception;

/**
 * Thrown when a duplicate attendance record is detected for the same employee and date.
 */
public class DuplicateAttendanceException extends RuntimeException {

    public DuplicateAttendanceException(String employeeId, String date) {
        super(String.format("Attendance already recorded for employee '%s' on %s", employeeId, date));
    }
}
