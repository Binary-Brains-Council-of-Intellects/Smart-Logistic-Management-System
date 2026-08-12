package com.binarybrains.slms.payroll.service;

import com.binarybrains.slms.common.exception.EmployeeNotFoundException;
import com.binarybrains.slms.employee.model.Employee;
import com.binarybrains.slms.employee.repository.EmployeeRepository;
import com.binarybrains.slms.payroll.dto.GeneratePayrollRequest;
import com.binarybrains.slms.payroll.dto.PayrollResponse;
import com.binarybrains.slms.payroll.model.Payroll;
import com.binarybrains.slms.payroll.repository.PayrollRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for payroll operations.
 * Uses POLYMORPHIC employee.calculatePayroll() to compute salary.
 *
 * This is the key demonstration of runtime polymorphism:
 *   Employee employee = employeeRepository.findById(id); // could be any subclass
 *   double grossPay = employee.calculatePayroll();        // dispatches to correct override
 */
@Service
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;

    public PayrollService(PayrollRepository payrollRepository, EmployeeRepository employeeRepository) {
        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * Generates payroll for a single employee.
     * The gross salary is computed via the polymorphic calculatePayroll() method.
     */
    public PayrollResponse generatePayroll(GeneratePayrollRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException(request.getEmployeeId()));

        // Check if payroll already generated for this period
        if (payrollRepository.existsByEmployeeIdAndMonthAndYear(
                request.getEmployeeId(), request.getMonth(), request.getYear())) {
            throw new IllegalStateException(String.format(
                    "Payroll already generated for employee '%s' for %d/%d",
                    employee.getName(), request.getMonth(), request.getYear()));
        }

        // POLYMORPHISM: calculatePayroll() dispatches to the correct subclass method
        double grossSalary = employee.calculatePayroll();

        double bonus = request.getAdditionalBonus();
        double deductions = request.getAdditionalDeductions();
        double overtimePay = grossSalary - employee.getBaseSalary(); // difference is the extra pay
        double netSalary = grossSalary + bonus - deductions;

        Payroll payroll = new Payroll(
                employee.getId(),
                employee.getName(),
                employee.getEmployeeType(),
                request.getMonth(),
                request.getYear(),
                employee.getBaseSalary(),
                bonus,
                deductions,
                overtimePay,
                grossSalary,
                netSalary
        );

        Payroll saved = payrollRepository.save(payroll);
        return PayrollResponse.fromPayroll(saved);
    }

    /**
     * Generates payroll for ALL active employees for a given month/year.
     */
    public List<PayrollResponse> generatePayrollForAll(int month, int year) {
        List<Employee> activeEmployees = employeeRepository.findByActiveTrue();

        return activeEmployees.stream()
                .filter(emp -> !payrollRepository.existsByEmployeeIdAndMonthAndYear(emp.getId(), month, year))
                .map(employee -> {
                    // POLYMORPHISM demonstrated in a loop
                    double grossSalary = employee.calculatePayroll();
                    double overtimePay = grossSalary - employee.getBaseSalary();
                    double netSalary = grossSalary;

                    Payroll payroll = new Payroll(
                            employee.getId(), employee.getName(), employee.getEmployeeType(),
                            month, year, employee.getBaseSalary(), 0, 0,
                            overtimePay, grossSalary, netSalary
                    );
                    return PayrollResponse.fromPayroll(payrollRepository.save(payroll));
                })
                .collect(Collectors.toList());
    }

    public PayrollResponse getPayrollById(String id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll record not found: " + id));
        return PayrollResponse.fromPayroll(payroll);
    }

    public List<PayrollResponse> getPayrollByEmployee(String employeeId) {
        return payrollRepository.findByEmployeeId(employeeId).stream()
                .map(PayrollResponse::fromPayroll)
                .collect(Collectors.toList());
    }

    public List<PayrollResponse> getPayrollByPeriod(int month, int year) {
        return payrollRepository.findByMonthAndYear(month, year).stream()
                .map(PayrollResponse::fromPayroll)
                .collect(Collectors.toList());
    }

    public List<PayrollResponse> getAllPayroll() {
        return payrollRepository.findAll().stream()
                .map(PayrollResponse::fromPayroll)
                .collect(Collectors.toList());
    }
}
