package com.binarybrains.slms.employee.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

/**
 * ====================================================================
 * CONCRETE CLASS: DeliveryDriver
 * ====================================================================
 * OOP: INHERITANCE + METHOD OVERRIDING + POLYMORPHISM
 *
 * Delivery driver payroll includes:
 *   baseSalary + (deliveryCount × perDeliveryBonus)
 * ====================================================================
 */
@Document(collection = "employees")
public class DeliveryDriver extends Employee {

    @Field("license_number")
    private String licenseNumber;

    @Field("vehicle_type")
    private String vehicleType;

    @Field("delivery_count")
    private int deliveryCount;

    @Field("per_delivery_bonus")
    private double perDeliveryBonus;

    // ====================================================================
    // CONSTRUCTORS
    // ====================================================================

    public DeliveryDriver() {
        super();
    }

    public DeliveryDriver(String name, String email, String phone, String department,
                          double baseSalary, LocalDate hireDate,
                          String licenseNumber, String vehicleType,
                          int deliveryCount, double perDeliveryBonus) {
        super(name, email, phone, department, baseSalary, EmployeeType.DELIVERY_DRIVER, hireDate);
        this.licenseNumber = licenseNumber;
        this.vehicleType = vehicleType;
        this.deliveryCount = deliveryCount;
        this.perDeliveryBonus = perDeliveryBonus;
    }

    // ====================================================================
    // POLYMORPHIC METHOD OVERRIDE
    // ====================================================================

    /**
     * Delivery driver payroll = base salary + delivery bonus.
     * Delivery bonus = deliveryCount × perDeliveryBonus.
     */
    @Override
    public double calculatePayroll() {
        double deliveryBonus = deliveryCount * perDeliveryBonus;
        return getBaseSalary() + deliveryBonus;
    }

    @Override
    public String getRoleDescription() {
        return "Delivery Driver - " + vehicleType + " (License: " + licenseNumber + ")";
    }

    // ====================================================================
    // GETTERS AND SETTERS
    // ====================================================================

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getDeliveryCount() {
        return deliveryCount;
    }

    public void setDeliveryCount(int deliveryCount) {
        this.deliveryCount = deliveryCount;
    }

    public double getPerDeliveryBonus() {
        return perDeliveryBonus;
    }

    public void setPerDeliveryBonus(double perDeliveryBonus) {
        this.perDeliveryBonus = perDeliveryBonus;
    }
}
