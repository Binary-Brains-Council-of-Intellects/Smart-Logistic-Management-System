package com.binarybrains.slms.employee.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

/**
 * ====================================================================
 * CONCRETE CLASS: Manager
 * ====================================================================
 * OOP: INHERITANCE + METHOD OVERRIDING + POLYMORPHISM
 *
 * Manager payroll includes:
 *   baseSalary + managementBonus + (teamSize × teamLeadBonus)
 * ====================================================================
 */
@Document(collection = "employees")
public class Manager extends Employee {

    @Field("managed_department")
    private String managedDepartment;

    @Field("team_size")
    private int teamSize;

    @Field("management_bonus")
    private double managementBonus;

    @Field("team_lead_bonus_per_member")
    private double teamLeadBonusPerMember;

    // ====================================================================
    // CONSTRUCTORS
    // ====================================================================

    public Manager() {
        super();
    }

    public Manager(String name, String email, String phone, String department,
                   double baseSalary, LocalDate hireDate,
                   String managedDepartment, int teamSize,
                   double managementBonus, double teamLeadBonusPerMember) {
        super(name, email, phone, department, baseSalary, EmployeeType.MANAGER, hireDate);
        this.managedDepartment = managedDepartment;
        this.teamSize = teamSize;
        this.managementBonus = managementBonus;
        this.teamLeadBonusPerMember = teamLeadBonusPerMember;
    }

    // ====================================================================
    // POLYMORPHIC METHOD OVERRIDE
    // ====================================================================

    /**
     * Manager payroll = base salary + management bonus + team leadership bonus.
     * Team leadership bonus = teamSize × teamLeadBonusPerMember.
     */
    @Override
    public double calculatePayroll() {
        double teamBonus = teamSize * teamLeadBonusPerMember;
        return getBaseSalary() + managementBonus + teamBonus;
    }

    @Override
    public String getRoleDescription() {
        return "Manager - " + managedDepartment + " (Team: " + teamSize + " members)";
    }

    // ====================================================================
    // GETTERS AND SETTERS
    // ====================================================================

    public String getManagedDepartment() {
        return managedDepartment;
    }

    public void setManagedDepartment(String managedDepartment) {
        this.managedDepartment = managedDepartment;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public double getManagementBonus() {
        return managementBonus;
    }

    public void setManagementBonus(double managementBonus) {
        this.managementBonus = managementBonus;
    }

    public double getTeamLeadBonusPerMember() {
        return teamLeadBonusPerMember;
    }

    public void setTeamLeadBonusPerMember(double teamLeadBonusPerMember) {
        this.teamLeadBonusPerMember = teamLeadBonusPerMember;
    }
}
