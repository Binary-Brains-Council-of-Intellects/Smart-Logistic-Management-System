package com.binarybrains.slms.employee.dto;

import com.binarybrains.slms.employee.model.EmployeeType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * DTO for creating a new employee.
 */
public class CreateEmployeeRequest {

    @NotBlank(message = "Employee name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    private String department;

    @Positive(message = "Base salary must be positive")
    private double baseSalary;

    @NotNull(message = "Employee type is required")
    private EmployeeType employeeType;

    private LocalDate hireDate;

    // WarehouseStaff-specific
    private String warehouseSection;
    private String shiftType;
    private Double overtimeHours;
    private Double overtimeRate;

    // DeliveryDriver-specific
    private String licenseNumber;
    private String vehicleType;
    private Integer deliveryCount;
    private Double perDeliveryBonus;

    // Manager-specific
    private String managedDepartment;
    private Integer teamSize;
    private Double managementBonus;
    private Double teamLeadBonusPerMember;

    // --- Getters and Setters ---

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(double baseSalary) { this.baseSalary = baseSalary; }

    public EmployeeType getEmployeeType() { return employeeType; }
    public void setEmployeeType(EmployeeType employeeType) { this.employeeType = employeeType; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public String getWarehouseSection() { return warehouseSection; }
    public void setWarehouseSection(String warehouseSection) { this.warehouseSection = warehouseSection; }

    public String getShiftType() { return shiftType; }
    public void setShiftType(String shiftType) { this.shiftType = shiftType; }

    public Double getOvertimeHours() { return overtimeHours; }
    public void setOvertimeHours(Double overtimeHours) { this.overtimeHours = overtimeHours; }

    public Double getOvertimeRate() { return overtimeRate; }
    public void setOvertimeRate(Double overtimeRate) { this.overtimeRate = overtimeRate; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public Integer getDeliveryCount() { return deliveryCount; }
    public void setDeliveryCount(Integer deliveryCount) { this.deliveryCount = deliveryCount; }

    public Double getPerDeliveryBonus() { return perDeliveryBonus; }
    public void setPerDeliveryBonus(Double perDeliveryBonus) { this.perDeliveryBonus = perDeliveryBonus; }

    public String getManagedDepartment() { return managedDepartment; }
    public void setManagedDepartment(String managedDepartment) { this.managedDepartment = managedDepartment; }

    public Integer getTeamSize() { return teamSize; }
    public void setTeamSize(Integer teamSize) { this.teamSize = teamSize; }

    public Double getManagementBonus() { return managementBonus; }
    public void setManagementBonus(Double managementBonus) { this.managementBonus = managementBonus; }

    public Double getTeamLeadBonusPerMember() { return teamLeadBonusPerMember; }
    public void setTeamLeadBonusPerMember(Double teamLeadBonusPerMember) { this.teamLeadBonusPerMember = teamLeadBonusPerMember; }
}
