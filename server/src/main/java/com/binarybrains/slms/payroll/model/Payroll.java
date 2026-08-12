package com.binarybrains.slms.payroll.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.binarybrains.slms.employee.model.EmployeeType;
import java.time.LocalDateTime;

@Document(collection = "payroll")
public class Payroll {

    @Id
    private String id;

    @Field("employee_id")
    private String employeeId;

    @Field("employee_name")
    private String employeeName;

    @Field("employee_type")
    private EmployeeType employeeType;

    @Field("month")
    private int month;

    @Field("year")
    private int year;

    @Field("base_salary")
    private double baseSalary;

    @Field("bonus")
    private double bonus;

    @Field("deductions")
    private double deductions;

    @Field("overtime_pay")
    private double overtimePay;

    @Field("gross_salary")
    private double grossSalary;

    @Field("net_salary")
    private double netSalary;

    @Field("generated_at")
    private LocalDateTime generatedAt;

    public Payroll() {
        this.generatedAt = LocalDateTime.now();
    }

    public Payroll(String employeeId, String employeeName, EmployeeType employeeType,
                   int month, int year, double baseSalary, double bonus,
                   double deductions, double overtimePay, double grossSalary, double netSalary) {
        this();
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeType = employeeType;
        this.month = month;
        this.year = year;
        this.baseSalary = baseSalary;
        this.bonus = bonus;
        this.deductions = deductions;
        this.overtimePay = overtimePay;
        this.grossSalary = grossSalary;
        this.netSalary = netSalary;
    }

    // --- Getters and Setters ---

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
