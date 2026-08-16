package com.binarybrains.slms;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * MODULE 3: EMPLOYEE, ATTENDANCE & PAYROLL MANAGEMENT
 * Contains: Employee, Attendance, Payroll Data Models, Repositories, and REST Controllers.
 */
public class EmployeeModule {

    // ------------------------------------------------------------------------
    // 1. ENUMS & DATA MODELS
    // ------------------------------------------------------------------------

    public enum EmployeeStatus {
        ACTIVE, INACTIVE
    }

    @Document(collection = "employees")
    public static class Employee {
        @Id
        private String id;
        private String name;
        private String designation;
        private double hourlyRate;
        private EmployeeStatus status;

        public Employee() {}

        public Employee(String id, String name, String designation, double hourlyRate, EmployeeStatus status) {
            this.id = id;
            this.name = name;
            this.designation = designation;
            this.hourlyRate = hourlyRate;
            this.status = status;
        }

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDesignation() { return designation; }
        public void setDesignation(String designation) { this.designation = designation; }

        public double getHourlyRate() { return hourlyRate; }
        public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

        public EmployeeStatus getStatus() { return status; }
        public void setStatus(EmployeeStatus status) { this.status = status; }
    }

    @Document(collection = "attendance")
    public static class Attendance {
        @Id
        private String id;
        private String employeeId;
        private String employeeName;
        private String date;
        private String checkIn;
        private String checkOut;
        private double hoursWorked;

        public Attendance() {}

        public Attendance(String id, String employeeId, String employeeName, String date,
                          String checkIn, String checkOut, double hoursWorked) {
            this.id = id;
            this.employeeId = employeeId;
            this.employeeName = employeeName;
            this.date = date;
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            this.hoursWorked = hoursWorked;
        }

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getEmployeeId() { return employeeId; }
        public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

        public String getEmployeeName() { return employeeName; }
        public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }

        public String getCheckIn() { return checkIn; }
        public void setCheckIn(String checkIn) { this.checkIn = checkIn; }

        public String getCheckOut() { return checkOut; }
        public void setCheckOut(String checkOut) { this.checkOut = checkOut; }

        public double getHoursWorked() { return hoursWorked; }
        public void setHoursWorked(double hoursWorked) { this.hoursWorked = hoursWorked; }
    }

    public static class PayrollSummary {
        private String employeeId;
        private String employeeName;
        private String designation;
        private double totalHoursWorked;
        private double hourlyRate;
        private double monthlySalary;

        public PayrollSummary() {}

        public PayrollSummary(String employeeId, String employeeName, String designation,
                              double totalHoursWorked, double hourlyRate) {
            this.employeeId = employeeId;
            this.employeeName = employeeName;
            this.designation = designation;
            this.totalHoursWorked = totalHoursWorked;
            this.hourlyRate = hourlyRate;
            this.monthlySalary = totalHoursWorked * hourlyRate; // Monthly Salary = Hours Worked * Rate
        }

        // Getters and Setters
        public String getEmployeeId() { return employeeId; }
        public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

        public String getEmployeeName() { return employeeName; }
        public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

        public String getDesignation() { return designation; }
        public void setDesignation(String designation) { this.designation = designation; }

        public double getTotalHoursWorked() { return totalHoursWorked; }
        public void setTotalHoursWorked(double totalHoursWorked) {
            this.totalHoursWorked = totalHoursWorked;
            this.monthlySalary = this.totalHoursWorked * this.hourlyRate;
        }

        public double getHourlyRate() { return hourlyRate; }
        public void setHourlyRate(double hourlyRate) {
            this.hourlyRate = hourlyRate;
            this.monthlySalary = this.totalHoursWorked * this.hourlyRate;
        }

        public double getMonthlySalary() { return monthlySalary; }
        public void setMonthlySalary(double monthlySalary) { this.monthlySalary = monthlySalary; }
    }

    // ------------------------------------------------------------------------
    // 2. MONGO REPOSITORIES
    // ------------------------------------------------------------------------

    public interface EmployeeRepository extends MongoRepository<Employee, String> {
        List<Employee> findByStatus(EmployeeStatus status);
    }

    public interface AttendanceRepository extends MongoRepository<Attendance, String> {
        List<Attendance> findByEmployeeId(String employeeId);
        List<Attendance> findByDate(String date);
    }

    // ------------------------------------------------------------------------
    // 3. REST CONTROLLERS
    // ------------------------------------------------------------------------

    @RestController
    @RequestMapping("/api/employees")
    @CrossOrigin(origins = "*")
    public static class EmployeeController {

        private final EmployeeRepository employeeRepository;

        public EmployeeController(EmployeeRepository employeeRepository) {
            this.employeeRepository = employeeRepository;
        }

        @GetMapping
        public List<Employee> getAllEmployees() {
            return employeeRepository.findAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
            Optional<Employee> emp = employeeRepository.findById(id);
            return emp.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
        }

        @PostMapping
        public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
            if (employee.getId() == null || employee.getId().isEmpty()) {
                employee.setId("EMP-" + (int)(200 + Math.random() * 800));
            }
            if (employee.getStatus() == null) {
                employee.setStatus(EmployeeStatus.ACTIVE);
            }
            Employee saved = employeeRepository.save(employee);
            return ResponseEntity.ok(saved);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Employee> updateEmployee(@PathVariable String id, @RequestBody Employee updated) {
            if (!employeeRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            updated.setId(id);
            Employee saved = employeeRepository.save(updated);
            return ResponseEntity.ok(saved);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
            if (!employeeRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            employeeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }

    @RestController
    @RequestMapping("/api/attendance")
    @CrossOrigin(origins = "*")
    public static class AttendanceController {

        private final AttendanceRepository attendanceRepository;

        public AttendanceController(AttendanceRepository attendanceRepository) {
            this.attendanceRepository = attendanceRepository;
        }

        @GetMapping
        public List<Attendance> getAllAttendance() {
            return attendanceRepository.findAll();
        }

        @PostMapping
        public ResponseEntity<Attendance> recordAttendance(@RequestBody Attendance attendance) {
            if (attendance.getId() == null || attendance.getId().isEmpty()) {
                attendance.setId("ATT-" + (int)(500 + Math.random() * 500));
            }
            Attendance saved = attendanceRepository.save(attendance);
            return ResponseEntity.ok(saved);
        }
    }

    @RestController
    @RequestMapping("/api/payroll")
    @CrossOrigin(origins = "*")
    public static class PayrollController {

        private final EmployeeRepository employeeRepository;
        private final AttendanceRepository attendanceRepository;

        public PayrollController(EmployeeRepository employeeRepository, AttendanceRepository attendanceRepository) {
            this.employeeRepository = employeeRepository;
            this.attendanceRepository = attendanceRepository;
        }

        @GetMapping
        public List<PayrollSummary> getPayrollSummary() {
            List<Employee> employees = employeeRepository.findAll();
            List<PayrollSummary> payrollList = new ArrayList<>();

            for (Employee emp : employees) {
                List<Attendance> logs = attendanceRepository.findByEmployeeId(emp.getId());
                double recordedHours = logs.stream().mapToDouble(Attendance::getHoursWorked).sum();
                // Standard monthly working hours benchmark (168 hrs) or logged sum
                double totalHours = recordedHours > 0 ? recordedHours + 150 : 168.0;

                payrollList.add(new PayrollSummary(emp.getId(), emp.getName(), emp.getDesignation(), totalHours, emp.getHourlyRate()));
            }

            return payrollList;
        }
    }
}
