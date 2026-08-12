package com.binarybrains.slms.payroll.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class GeneratePayrollRequest {

    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private int month;

    @Min(value = 2000, message = "Year must be valid")
    private int year;

    private double additionalBonus;
    private double additionalDeductions;

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public double getAdditionalBonus() { return additionalBonus; }
    public void setAdditionalBonus(double additionalBonus) { this.additionalBonus = additionalBonus; }

    public double getAdditionalDeductions() { return additionalDeductions; }
    public void setAdditionalDeductions(double additionalDeductions) { this.additionalDeductions = additionalDeductions; }
}
