package com.binarybrains.slms.report.dto;

public class WorkforceReportDTO {
    private long totalEmployees;
    private long activeEmployees;
    private long warehouseStaffCount;
    private long deliveryDriverCount;
    private long managerCount;
    private double totalPayroll;
    private double averageSalary;

    public long getTotalEmployees() { return totalEmployees; }
    public void setTotalEmployees(long v) { this.totalEmployees = v; }
    public long getActiveEmployees() { return activeEmployees; }
    public void setActiveEmployees(long v) { this.activeEmployees = v; }
    public long getWarehouseStaffCount() { return warehouseStaffCount; }
    public void setWarehouseStaffCount(long v) { this.warehouseStaffCount = v; }
    public long getDeliveryDriverCount() { return deliveryDriverCount; }
    public void setDeliveryDriverCount(long v) { this.deliveryDriverCount = v; }
    public long getManagerCount() { return managerCount; }
    public void setManagerCount(long v) { this.managerCount = v; }
    public double getTotalPayroll() { return totalPayroll; }
    public void setTotalPayroll(double v) { this.totalPayroll = v; }
    public double getAverageSalary() { return averageSalary; }
    public void setAverageSalary(double v) { this.averageSalary = v; }
}
