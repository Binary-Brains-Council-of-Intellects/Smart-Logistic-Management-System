package com.binarybrains.slms.payroll.dto;

import com.binarybrains.slms.employee.model.EmployeeType;
import com.binarybrains.slms.payroll.model.Payroll;

import java.time.LocalDateTime;

public class PayrollResponse {

    private String id;
    private String employeeId;
    private String employeeName;
    private EmployeeType employeeType;
    private int month;
    private int year;
    private double baseSalary;
    private double bonus;
    private double deductions;
    private double overtimePay;
    private double grossSalary;
    private double netSalary;
    private LocalDateTime generatedAt;

    public static PayrollResponse fromPayroll(Payroll payroll) {
        PayrollResponse r = new PayrollResponse();
        r.id = payroll.getId();
        r.employeeId = payroll.getEmployeeId();
        r.employeeName = payroll.getEmployeeName();
        r.employeeType = payroll.getEmployeeType();
        r.month = payroll.getMonth();
        r.year = payroll.getYear();
        r.baseSalary = payroll.getBaseSalary();
        r.bonus = payroll.getBonus();
        r.deductions = payroll.getDeductions();
        r.overtimePay = payroll.getOvertimePay();
        r.grossSalary = payroll.getGrossSalary();
        r.netSalary = payroll.getNetSalary();
        r.generatedAt = payroll.getGeneratedAt();
        return r;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public EmployeeType getEmployeeType() { return employeeType; }
    public void setEmployeeType(EmployeeType employeeType) { this.employeeType = employeeType; }
    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(double baseSalary) { this.baseSalary = baseSalary; }
    public double getBonus() { return bonus; }
    public void setBonus(double bonus) { this.bonus = bonus; }
    public double getDeductions() { return deductions; }
    public void setDeductions(double deductions) { this.deductions = deductions; }
    public double getOvertimePay() { return overtimePay; }
    public void setOvertimePay(double overtimePay) { this.overtimePay = overtimePay; }
    public double getGrossSalary() { return grossSalary; }
    public void setGrossSalary(double grossSalary) { this.grossSalary = grossSalary; }
    public double getNetSalary() { return netSalary; }
    public void setNetSalary(double netSalary) { this.netSalary = netSalary; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
}
