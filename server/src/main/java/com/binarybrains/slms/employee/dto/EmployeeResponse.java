package com.binarybrains.slms.employee.dto;

import com.binarybrains.slms.employee.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for employee API responses.
 */
public class EmployeeResponse {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String department;
    private double baseSalary;
    private EmployeeType employeeType;
    private LocalDate hireDate;
    private boolean active;
    private String roleDescription;
    private double calculatedPayroll;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Subclass-specific fields
    private String warehouseSection;
    private String shiftType;
    private Double overtimeHours;
    private Double overtimeRate;

    private String licenseNumber;
    private String vehicleType;
    private Integer deliveryCount;
    private Double perDeliveryBonus;

    private String managedDepartment;
    private Integer teamSize;
    private Double managementBonus;
    private Double teamLeadBonusPerMember;

    /**
     * Converts domain model to response DTO.
     * Calls the polymorphic calculatePayroll() and getRoleDescription().
     */
    public static EmployeeResponse fromEmployee(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.id = employee.getId();
        response.name = employee.getName();
        response.email = employee.getEmail();
        response.phone = employee.getPhone();
        response.department = employee.getDepartment();
        response.baseSalary = employee.getBaseSalary();
        response.employeeType = employee.getEmployeeType();
        response.hireDate = employee.getHireDate();
        response.active = employee.isActive();
        response.roleDescription = employee.getRoleDescription();
        response.calculatedPayroll = employee.calculatePayroll(); // POLYMORPHISM
        response.createdAt = employee.getCreatedAt();
        response.updatedAt = employee.getUpdatedAt();

        if (employee instanceof WarehouseStaff ws) {
            response.warehouseSection = ws.getWarehouseSection();
            response.shiftType = ws.getShiftType();
            response.overtimeHours = ws.getOvertimeHours();
            response.overtimeRate = ws.getOvertimeRate();
        }

        if (employee instanceof DeliveryDriver dd) {
            response.licenseNumber = dd.getLicenseNumber();
            response.vehicleType = dd.getVehicleType();
            response.deliveryCount = dd.getDeliveryCount();
            response.perDeliveryBonus = dd.getPerDeliveryBonus();
        }

        if (employee instanceof Manager mgr) {
            response.managedDepartment = mgr.getManagedDepartment();
            response.teamSize = mgr.getTeamSize();
            response.managementBonus = mgr.getManagementBonus();
            response.teamLeadBonusPerMember = mgr.getTeamLeadBonusPerMember();
        }

        return response;
    }

    // --- Getters and Setters ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getRoleDescription() { return roleDescription; }
    public void setRoleDescription(String roleDescription) { this.roleDescription = roleDescription; }

    public double getCalculatedPayroll() { return calculatedPayroll; }
    public void setCalculatedPayroll(double calculatedPayroll) { this.calculatedPayroll = calculatedPayroll; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

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
