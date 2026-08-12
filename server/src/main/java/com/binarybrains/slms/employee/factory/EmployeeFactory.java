package com.binarybrains.slms.employee.factory;

import com.binarybrains.slms.employee.dto.CreateEmployeeRequest;
import com.binarybrains.slms.employee.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Factory for creating the appropriate Employee subclass.
 * Mirrors the ProductFactory pattern.
 */
@Component
public class EmployeeFactory {

    public Employee createEmployee(CreateEmployeeRequest request) {
        return switch (request.getEmployeeType()) {
            case WAREHOUSE_STAFF -> createWarehouseStaff(request);
            case DELIVERY_DRIVER -> createDeliveryDriver(request);
            case MANAGER -> createManager(request);
        };
    }

    private WarehouseStaff createWarehouseStaff(CreateEmployeeRequest req) {
        return new WarehouseStaff(
                req.getName(), req.getEmail(), req.getPhone(), req.getDepartment(),
                req.getBaseSalary(),
                req.getHireDate() != null ? req.getHireDate() : LocalDate.now(),
                req.getWarehouseSection() != null ? req.getWarehouseSection() : "General",
                req.getShiftType() != null ? req.getShiftType() : "DAY",
                req.getOvertimeHours() != null ? req.getOvertimeHours() : 0.0,
                req.getOvertimeRate() != null ? req.getOvertimeRate() : 1.5
        );
    }

    private DeliveryDriver createDeliveryDriver(CreateEmployeeRequest req) {
        return new DeliveryDriver(
                req.getName(), req.getEmail(), req.getPhone(), req.getDepartment(),
                req.getBaseSalary(),
                req.getHireDate() != null ? req.getHireDate() : LocalDate.now(),
                req.getLicenseNumber() != null ? req.getLicenseNumber() : "",
                req.getVehicleType() != null ? req.getVehicleType() : "Van",
                req.getDeliveryCount() != null ? req.getDeliveryCount() : 0,
                req.getPerDeliveryBonus() != null ? req.getPerDeliveryBonus() : 50.0
        );
    }

    private Manager createManager(CreateEmployeeRequest req) {
        return new Manager(
                req.getName(), req.getEmail(), req.getPhone(), req.getDepartment(),
                req.getBaseSalary(),
                req.getHireDate() != null ? req.getHireDate() : LocalDate.now(),
                req.getManagedDepartment() != null ? req.getManagedDepartment() : req.getDepartment(),
                req.getTeamSize() != null ? req.getTeamSize() : 0,
                req.getManagementBonus() != null ? req.getManagementBonus() : 5000.0,
                req.getTeamLeadBonusPerMember() != null ? req.getTeamLeadBonusPerMember() : 500.0
        );
    }
}
