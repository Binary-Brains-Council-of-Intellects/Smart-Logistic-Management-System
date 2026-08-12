package com.binarybrains.slms.employee.service;

import com.binarybrains.slms.common.exception.EmployeeNotFoundException;
import com.binarybrains.slms.employee.dto.CreateEmployeeRequest;
import com.binarybrains.slms.employee.dto.EmployeeResponse;
import com.binarybrains.slms.employee.dto.UpdateEmployeeRequest;
import com.binarybrains.slms.employee.factory.EmployeeFactory;
import com.binarybrains.slms.employee.model.*;
import com.binarybrains.slms.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeFactory employeeFactory;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeFactory employeeFactory) {
        this.employeeRepository = employeeRepository;
        this.employeeFactory = employeeFactory;
    }

    @Override
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("An employee with email '" + request.getEmail() + "' already exists");
        }

        Employee employee = employeeFactory.createEmployee(request);
        Employee saved = employeeRepository.save(employee);
        return EmployeeResponse.fromEmployee(saved);
    }

    @Override
    public EmployeeResponse getEmployeeById(String id) {
        Employee employee = findEmployeeOrThrow(id);
        return EmployeeResponse.fromEmployee(employee);
    }

    @Override
    public Employee getEmployeeEntityById(String id) {
        return findEmployeeOrThrow(id);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(EmployeeResponse::fromEmployee)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeResponse> getActiveEmployees() {
        return employeeRepository.findByActiveTrue().stream()
                .map(EmployeeResponse::fromEmployee)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeResponse> getEmployeesByType(EmployeeType type) {
        return employeeRepository.findByEmployeeTypeAndActiveTrue(type).stream()
                .map(EmployeeResponse::fromEmployee)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeResponse> searchEmployees(String keyword) {
        return employeeRepository.searchByName(keyword).stream()
                .map(EmployeeResponse::fromEmployee)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponse updateEmployee(String id, UpdateEmployeeRequest request) {
        Employee employee = findEmployeeOrThrow(id);

        if (request.getName() != null) employee.setName(request.getName());
        if (request.getEmail() != null) employee.setEmail(request.getEmail());
        if (request.getPhone() != null) employee.setPhone(request.getPhone());
        if (request.getDepartment() != null) employee.setDepartment(request.getDepartment());
        if (request.getBaseSalary() != null) employee.setBaseSalary(request.getBaseSalary());

        // Update subclass-specific fields
        if (employee instanceof WarehouseStaff ws) {
            if (request.getWarehouseSection() != null) ws.setWarehouseSection(request.getWarehouseSection());
            if (request.getShiftType() != null) ws.setShiftType(request.getShiftType());
            if (request.getOvertimeHours() != null) ws.setOvertimeHours(request.getOvertimeHours());
            if (request.getOvertimeRate() != null) ws.setOvertimeRate(request.getOvertimeRate());
        }

        if (employee instanceof DeliveryDriver dd) {
            if (request.getLicenseNumber() != null) dd.setLicenseNumber(request.getLicenseNumber());
            if (request.getVehicleType() != null) dd.setVehicleType(request.getVehicleType());
            if (request.getDeliveryCount() != null) dd.setDeliveryCount(request.getDeliveryCount());
            if (request.getPerDeliveryBonus() != null) dd.setPerDeliveryBonus(request.getPerDeliveryBonus());
        }

        if (employee instanceof Manager mgr) {
            if (request.getManagedDepartment() != null) mgr.setManagedDepartment(request.getManagedDepartment());
            if (request.getTeamSize() != null) mgr.setTeamSize(request.getTeamSize());
            if (request.getManagementBonus() != null) mgr.setManagementBonus(request.getManagementBonus());
            if (request.getTeamLeadBonusPerMember() != null) mgr.setTeamLeadBonusPerMember(request.getTeamLeadBonusPerMember());
        }

        employee.setUpdatedAt(LocalDateTime.now());
        Employee saved = employeeRepository.save(employee);
        return EmployeeResponse.fromEmployee(saved);
    }

    @Override
    public void deactivateEmployee(String id) {
        Employee employee = findEmployeeOrThrow(id);
        employee.deactivate();
        employeeRepository.save(employee);
    }

    @Override
    public void activateEmployee(String id) {
        Employee employee = findEmployeeOrThrow(id);
        employee.activate();
        employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(String id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        employeeRepository.deleteById(id);
    }

    private Employee findEmployeeOrThrow(String id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }
}
