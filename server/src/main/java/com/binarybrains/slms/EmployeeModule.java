package com.binarybrains.slms;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Employee, Attendance & Payroll Module
 */
public class EmployeeModule {

    // Abstraction & Inheritance
    public static abstract class BaseEntity {
        // Encapsulation
        private String id;

        public BaseEntity() {}
        public BaseEntity(String id) {
            this.id = id;
        }

        // Encapsulation
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        // Abstraction
        public abstract String getSummaryDetails();
    }

    public enum EmployeeStatus {
        ACTIVE, INACTIVE
    }

    // Inheritance
    public static class Employee extends BaseEntity {

        // Encapsulation
        private String name;
        private String designation;
        private double hourlyRate;
        private EmployeeStatus status;

        // Polymorphism
        public Employee() { super(); }

        // Polymorphism
        public Employee(String id, String name, String designation, double hourlyRate, EmployeeStatus status) {
            super(id);
            this.name = name;
            this.designation = designation;
            setHourlyRate(hourlyRate);
            this.status = status;
        }

        // Polymorphism
        public Employee(String id, String name, String designation) {
            this(id, name, designation, 25.0, EmployeeStatus.ACTIVE);
        }

        // Polymorphism
        @Override
        public String getSummaryDetails() {
            return "Employee [" + getId() + "] " + name + " - " + designation + " (" + status + ")";
        }

        // Polymorphism
        public double calculateEarnings(double hoursWorked) {
            return hoursWorked * this.hourlyRate;
        }

        // Polymorphism
        public double calculateEarnings(double hoursWorked, double bonusRate) {
            return (hoursWorked * this.hourlyRate) * (1.0 + bonusRate);
        }

        // Encapsulation
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDesignation() { return designation; }
        public void setDesignation(String designation) { this.designation = designation; }

        public double getHourlyRate() { return hourlyRate; }
        public void setHourlyRate(double hourlyRate) {
            this.hourlyRate = Math.max(0.0, hourlyRate);
        }

        public EmployeeStatus getStatus() { return status; }
        public void setStatus(EmployeeStatus status) { this.status = status; }
    }

    // Inheritance
    public static class Attendance extends BaseEntity {

        // Encapsulation
        private String employeeId;
        private String employeeName;
        private String date;
        private String checkIn;
        private String checkOut;
        private double hoursWorked;

        // Polymorphism
        public Attendance() { super(); }

        // Polymorphism
        public Attendance(String id, String employeeId, String employeeName, String date,
                          String checkIn, String checkOut, double hoursWorked) {
            super(id);
            this.employeeId = employeeId;
            this.employeeName = employeeName;
            this.date = date;
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            setHoursWorked(hoursWorked);
        }

        // Polymorphism
        @Override
        public String getSummaryDetails() {
            return "Attendance Log [" + getId() + "] Employee: " + employeeName + " Date: " + date + " Hours: " + hoursWorked;
        }

        // Encapsulation
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
        public void setHoursWorked(double hoursWorked) {
            this.hoursWorked = Math.max(0.0, hoursWorked);
        }
    }

    // Composition
    public static class PayrollSummary {

        // Encapsulation
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
            recalculateMonthlySalary();
        }

        private void recalculateMonthlySalary() {
            this.monthlySalary = this.totalHoursWorked * this.hourlyRate;
        }

        // Encapsulation
        public String getEmployeeId() { return employeeId; }
        public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

        public String getEmployeeName() { return employeeName; }
        public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

        public String getDesignation() { return designation; }
        public void setDesignation(String designation) { this.designation = designation; }

        public double getTotalHoursWorked() { return totalHoursWorked; }
        public void setTotalHoursWorked(double totalHoursWorked) {
            this.totalHoursWorked = totalHoursWorked;
            recalculateMonthlySalary();
        }

        public double getHourlyRate() { return hourlyRate; }
        public void setHourlyRate(double hourlyRate) {
            this.hourlyRate = hourlyRate;
            recalculateMonthlySalary();
        }

        public double getMonthlySalary() { return monthlySalary; }
        public void setMonthlySalary(double monthlySalary) { this.monthlySalary = monthlySalary; }
    }

    // Abstraction
    public interface EmployeeRepository {
        List<Employee> findAll();
        Optional<Employee> findById(String id);
        List<Employee> findByStatus(EmployeeStatus status);
        Employee save(Employee employee);
        boolean deleteById(String id);
        boolean existsById(String id);
    }

    // Abstraction
    public interface AttendanceRepository {
        List<Attendance> findAll();
        List<Attendance> findByEmployeeId(String employeeId);
        Attendance save(Attendance attendance);
    }

    // Inheritance
    public static class InMemoryEmployeeRepository implements EmployeeRepository {
        private final Map<String, Employee> store = new ConcurrentHashMap<>();

        public InMemoryEmployeeRepository() {
            save(new Employee("EMP-201", "Rahim Ahmed", "Logistics Executive", 28.5, EmployeeStatus.ACTIVE));
            save(new Employee("EMP-202", "Karim Uddin", "Warehouse Supervisor", 32.0, EmployeeStatus.ACTIVE));
            save(new Employee("EMP-203", "Nusrat Jahan", "Inventory Analyst", 30.0, EmployeeStatus.ACTIVE));
        }

        @Override public List<Employee> findAll() { return new ArrayList<>(store.values()); }
        @Override public Optional<Employee> findById(String id) { return Optional.ofNullable(store.get(id)); }
        @Override public List<Employee> findByStatus(EmployeeStatus status) {
            List<Employee> list = new ArrayList<>();
            for (Employee e : store.values()) if (e.getStatus() == status) list.add(e);
            return list;
        }
        @Override public Employee save(Employee employee) { store.put(employee.getId(), employee); return employee; }
        @Override public boolean deleteById(String id) { return store.remove(id) != null; }
        @Override public boolean existsById(String id) { return store.containsKey(id); }
    }

    // Inheritance
    public static class InMemoryAttendanceRepository implements AttendanceRepository {
        private final List<Attendance> list = Collections.synchronizedList(new ArrayList<>());

        public InMemoryAttendanceRepository() {
            list.add(new Attendance("ATT-501", "EMP-201", "Rahim Ahmed", "2026-08-15", "09:00", "17:00", 8.0));
            list.add(new Attendance("ATT-502", "EMP-202", "Karim Uddin", "2026-08-15", "08:30", "16:30", 8.0));
        }

        @Override public List<Attendance> findAll() { return new ArrayList<>(list); }
        @Override public List<Attendance> findByEmployeeId(String employeeId) {
            List<Attendance> result = new ArrayList<>();
            synchronized (list) {
                for (Attendance a : list) if (Objects.equals(a.getEmployeeId(), employeeId)) result.add(a);
            }
            return result;
        }
        @Override public Attendance save(Attendance attendance) { list.add(attendance); return attendance; }
    }

    // Composition
    public static class EmployeeHttpHandler implements HttpHandler {
        private final EmployeeRepository employeeRepository;

        public EmployeeHttpHandler(EmployeeRepository employeeRepository) {
            this.employeeRepository = employeeRepository;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setupCors(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) { exchange.sendResponseHeaders(204, -1); return; }

            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] parts = path.split("/");
            String id = (parts.length > 3) ? parts[3] : null;

            try {
                if ("GET".equalsIgnoreCase(method)) {
                    if (id != null) {
                        Optional<Employee> emp = employeeRepository.findById(id);
                        if (emp.isPresent()) sendJsonResponse(exchange, 200, SlmsApplication.toJson(emp.get()));
                        else sendJsonResponse(exchange, 404, "{\"error\":\"Employee not found\"}");
                    } else {
                        sendJsonResponse(exchange, 200, SlmsApplication.toJson(employeeRepository.findAll()));
                    }
                } else if ("POST".equalsIgnoreCase(method)) {
                    String body = readBody(exchange);
                    Map<String, String> map = SlmsApplication.parseJsonMap(body);
                    Employee emp = new Employee();
                    emp.setId(map.containsKey("id") && !map.get("id").isEmpty() ? map.get("id") : "EMP-" + (int)(200 + Math.random() * 800));
                    emp.setName(map.getOrDefault("name", "New Employee"));
                    
                    String desig = map.containsKey("designation") ? map.get("designation") : map.getOrDefault("department", "Warehouse Operator");
                    double rate = map.containsKey("hourlyRate") ? Double.parseDouble(map.get("hourlyRate")) : (map.containsKey("baseSalary") ? Double.parseDouble(map.get("baseSalary")) : 200.0);
                    
                    emp.setDesignation(desig);
                    emp.setHourlyRate(rate);
                    try { emp.setStatus(EmployeeStatus.valueOf(map.getOrDefault("status", "ACTIVE"))); } catch (Exception e) { emp.setStatus(EmployeeStatus.ACTIVE); }
                    Employee saved = employeeRepository.save(emp);
                    sendJsonResponse(exchange, 200, SlmsApplication.toJson(saved));
                } else if ("PUT".equalsIgnoreCase(method)) {
                    if (id == null || !employeeRepository.existsById(id)) {
                        sendJsonResponse(exchange, 404, "{\"error\":\"Employee not found\"}");
                        return;
                    }
                    String body = readBody(exchange);
                    Map<String, String> map = SlmsApplication.parseJsonMap(body);
                    Optional<Employee> existing = employeeRepository.findById(id);
                    if (existing.isPresent()) {
                        Employee emp = existing.get();
                        if (map.containsKey("name")) emp.setName(map.get("name"));
                        if (map.containsKey("designation")) emp.setDesignation(map.get("designation"));
                        if (map.containsKey("hourlyRate")) emp.setHourlyRate(Double.parseDouble(map.get("hourlyRate")));
                        employeeRepository.save(emp);
                        sendJsonResponse(exchange, 200, SlmsApplication.toJson(emp));
                    }
                } else if ("DELETE".equalsIgnoreCase(method)) {
                    if (id != null && employeeRepository.deleteById(id)) {
                        exchange.sendResponseHeaders(204, -1);
                    } else {
                        sendJsonResponse(exchange, 404, "{\"error\":\"Employee not found\"}");
                    }
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"error\":\"" + e.getMessage() + "\"}");
            }
        }
    }

    // Composition
    public static class AttendanceHttpHandler implements HttpHandler {
        private final AttendanceRepository attendanceRepository;

        public AttendanceHttpHandler(AttendanceRepository attendanceRepository) {
            this.attendanceRepository = attendanceRepository;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setupCors(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) { exchange.sendResponseHeaders(204, -1); return; }

            String method = exchange.getRequestMethod();
            if ("GET".equalsIgnoreCase(method)) {
                sendJsonResponse(exchange, 200, SlmsApplication.toJson(attendanceRepository.findAll()));
            } else if ("POST".equalsIgnoreCase(method)) {
                String body = readBody(exchange);
                Map<String, String> map = SlmsApplication.parseJsonMap(body);
                Attendance att = new Attendance();
                att.setId(map.containsKey("id") && !map.get("id").isEmpty() ? map.get("id") : "ATT-" + (int)(500 + Math.random() * 500));
                att.setEmployeeId(map.getOrDefault("employeeId", "EMP-201"));
                att.setEmployeeName(map.getOrDefault("employeeName", "Employee"));
                att.setDate(map.containsKey("date") ? map.get("date") : java.time.LocalDate.now().toString());
                att.setCheckIn(map.getOrDefault("checkIn", "09:00"));
                att.setCheckOut(map.getOrDefault("checkOut", "17:00"));
                if (map.containsKey("hoursWorked")) {
                    try { att.setHoursWorked(Double.parseDouble(map.get("hoursWorked"))); } catch (Exception ignored) {}
                } else {
                    att.setHoursWorked(8.0);
                }
                Attendance saved = attendanceRepository.save(att);
                sendJsonResponse(exchange, 200, SlmsApplication.toJson(saved));
            }
        }
    }

    // Composition
    public static class PayrollHttpHandler implements HttpHandler {
        private final EmployeeRepository employeeRepository;
        private final AttendanceRepository attendanceRepository;

        public PayrollHttpHandler(EmployeeRepository employeeRepository, AttendanceRepository attendanceRepository) {
            this.employeeRepository = employeeRepository;
            this.attendanceRepository = attendanceRepository;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setupCors(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) { exchange.sendResponseHeaders(204, -1); return; }

            List<Employee> employees = employeeRepository.findAll();
            List<PayrollSummary> payrollList = new ArrayList<>();

            for (Employee emp : employees) {
                List<Attendance> logs = attendanceRepository.findByEmployeeId(emp.getId());
                double recordedHours = logs.stream().mapToDouble(Attendance::getHoursWorked).sum();
                double totalHours = recordedHours > 0 ? recordedHours + 150 : 168.0;
                payrollList.add(new PayrollSummary(emp.getId(), emp.getName(), emp.getDesignation(), totalHours, emp.getHourlyRate()));
            }

            sendJsonResponse(exchange, 200, SlmsApplication.toJson(payrollList));
        }
    }

    private static void setupCors(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
    }

    private static String readBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    private static void sendJsonResponse(HttpExchange exchange, int statusCode, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
