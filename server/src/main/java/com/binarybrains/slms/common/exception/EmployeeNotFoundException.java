package com.binarybrains.slms.common.exception;

public class EmployeeNotFoundException extends ResourceNotFoundException {

    public EmployeeNotFoundException(String employeeId) {
        super("Employee", "id", employeeId);
    }
}
