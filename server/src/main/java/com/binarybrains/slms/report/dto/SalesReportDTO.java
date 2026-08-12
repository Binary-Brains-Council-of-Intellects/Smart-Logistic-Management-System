package com.binarybrains.slms.report.dto;

public class SalesReportDTO {
    private long totalOrders;
    private long confirmedOrders;
    private long deliveredOrders;
    private long cancelledOrders;
    private double totalRevenue;
    private double averageOrderValue;

    public long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(long v) { this.totalOrders = v; }
    public long getConfirmedOrders() { return confirmedOrders; }
    public void setConfirmedOrders(long v) { this.confirmedOrders = v; }
    public long getDeliveredOrders() { return deliveredOrders; }
    public void setDeliveredOrders(long v) { this.deliveredOrders = v; }
    public long getCancelledOrders() { return cancelledOrders; }
    public void setCancelledOrders(long v) { this.cancelledOrders = v; }
    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double v) { this.totalRevenue = v; }
    public double getAverageOrderValue() { return averageOrderValue; }
    public void setAverageOrderValue(double v) { this.averageOrderValue = v; }
}
