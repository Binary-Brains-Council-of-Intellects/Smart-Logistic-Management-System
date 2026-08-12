package com.binarybrains.slms.employee.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ====================================================================
 * ABSTRACT CLASS: Employee
 * ====================================================================
 * OOP Concepts demonstrated:
 *   - ABSTRACTION: Cannot be instantiated directly.
 *   - ENCAPSULATION: All fields private with controlled access.
 *   - INHERITANCE: WarehouseStaff, DeliveryDriver, Manager extend this.
 *   - POLYMORPHISM: calculatePayroll() is abstract — each subclass
 *     provides its own salary calculation logic.
 *
 * MongoDB: Single "employees" collection with "_class" discriminator.
 * ====================================================================
 */
@Document(collection = "employees")
public abstract class Employee {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Indexed(unique = true)
    @Field("email")
    private String email;

    @Field("phone")
    private String phone;

    @Field("department")
    private String department;

    @Field("base_salary")
    private double baseSalary;

    @Indexed
    @Field("employee_type")
    private EmployeeType employeeType;

    @Field("hire_date")
    private LocalDate hireDate;

    @Field("active")
    private boolean active;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    // ====================================================================
    // CONSTRUCTORS
    // ====================================================================

    protected Employee() {
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    protected Employee(String name, String email, String phone, String department,
                       double baseSalary, EmployeeType employeeType, LocalDate hireDate) {
        this();
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.baseSalary = baseSalary;
        this.employeeType = employeeType;
        this.hireDate = hireDate;
    }

    // ====================================================================
    // ABSTRACT METHOD — POLYMORPHISM
    // Each subclass computes payroll differently.
    //
    // Usage:
    //   List<Employee> employees = ...;
    //   for (Employee e : employees) {
    //       double salary = e.calculatePayroll(); // Runtime polymorphism
    //   }
    // ====================================================================

    /**
     * Calculates the total payroll amount for this employee.
     * This is the KEY POLYMORPHIC method in the employee hierarchy.
     *
     * @return the total salary/payroll amount
     */
    public abstract double calculatePayroll();

    /**
     * Returns a description of the employee's role.
     * Can be overridden by subclasses for specific role info.
     */
    public String getRoleDescription() {
        return employeeType + " in " + department;
    }

    // ====================================================================
    // DOMAIN METHODS
    // ====================================================================

    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    // ====================================================================
    // GETTERS
    // ====================================================================

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDepartment() {
        return department;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // ====================================================================
    // SETTERS
    // ====================================================================

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return String.format("Employee{id='%s', name='%s', type=%s, salary=%.2f}",
                id, name, employeeType, baseSalary);
    }
}
