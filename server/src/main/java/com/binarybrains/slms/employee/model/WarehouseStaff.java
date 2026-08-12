package com.binarybrains.slms.employee.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

/**
 * ====================================================================
 * CONCRETE CLASS: WarehouseStaff
 * ====================================================================
 * OOP: INHERITANCE + METHOD OVERRIDING + POLYMORPHISM
 *
 * Warehouse staff payroll includes:
 *   baseSalary + (overtimeHours × overtimeRate)
 * ====================================================================
 */
@Document(collection = "employees")
public class WarehouseStaff extends Employee {

    @Field("warehouse_section")
    private String warehouseSection;

    @Field("shift_type")
    private String shiftType; // DAY, NIGHT, ROTATING

    @Field("overtime_hours")
    private double overtimeHours;

    @Field("overtime_rate")
    private double overtimeRate; // multiplier, e.g., 1.5

    // ====================================================================
    // CONSTRUCTORS
    // ====================================================================

    public WarehouseStaff() {
        super();
    }

    public WarehouseStaff(String name, String email, String phone, String department,
                          double baseSalary, LocalDate hireDate,
                          String warehouseSection, String shiftType,
                          double overtimeHours, double overtimeRate) {
        super(name, email, phone, department, baseSalary, EmployeeType.WAREHOUSE_STAFF, hireDate);
        this.warehouseSection = warehouseSection;
        this.shiftType = shiftType;
        this.overtimeHours = overtimeHours;
        this.overtimeRate = overtimeRate;
    }

    // ====================================================================
    // POLYMORPHIC METHOD OVERRIDE
    // ====================================================================

    /**
     * Warehouse staff payroll = base salary + overtime pay.
     * Overtime pay = overtimeHours × (baseSalary/160) × overtimeRate
     * where 160 = approximate monthly working hours.
     */
    @Override
    public double calculatePayroll() {
        double hourlyRate = getBaseSalary() / 160.0;
        double overtimePay = overtimeHours * hourlyRate * overtimeRate;
        return getBaseSalary() + overtimePay;
    }

    @Override
    public String getRoleDescription() {
        return "Warehouse Staff - " + warehouseSection + " (" + shiftType + " shift)";
    }

    // ====================================================================
    // GETTERS AND SETTERS
    // ====================================================================

    public String getWarehouseSection() {
        return warehouseSection;
    }

    public void setWarehouseSection(String warehouseSection) {
        this.warehouseSection = warehouseSection;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public double getOvertimeRate() {
        return overtimeRate;
    }

    public void setOvertimeRate(double overtimeRate) {
        this.overtimeRate = overtimeRate;
    }
}
