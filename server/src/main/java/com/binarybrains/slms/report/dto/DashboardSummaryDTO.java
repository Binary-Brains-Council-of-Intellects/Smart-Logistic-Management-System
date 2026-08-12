package com.binarybrains.slms.report.dto;

public class DashboardSummaryDTO {
    private long totalProducts;
    private long totalOrders;
    private long totalCustomers;
    private long totalEmployees;
    private double totalRevenue;
    private long pendingOrders;
    private long completedOrders;
    private long cancelledOrders;
    private long expiredProducts;
    private long lowStockProducts;
    private long pendingReturns;
    private double averageProductRating;

    // Getters and setters
    public long getTotalProducts() { return totalProducts; }
    public void setTotalProducts(long v) { this.totalProducts = v; }
    public long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(long v) { this.totalOrders = v; }
    public long getTotalCustomers() { return totalCustomers; }
    public void setTotalCustomers(long v) { this.totalCustomers = v; }
    public long getTotalEmployees() { return totalEmployees; }
    public void setTotalEmployees(long v) { this.totalEmployees = v; }
    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double v) { this.totalRevenue = v; }
    public long getPendingOrders() { return pendingOrders; }
    public void setPendingOrders(long v) { this.pendingOrders = v; }
    public long getCompletedOrders() { return completedOrders; }
    public void setCompletedOrders(long v) { this.completedOrders = v; }
    public long getCancelledOrders() { return cancelledOrders; }
    public void setCancelledOrders(long v) { this.cancelledOrders = v; }
    public long getExpiredProducts() { return expiredProducts; }
    public void setExpiredProducts(long v) { this.expiredProducts = v; }
    public long getLowStockProducts() { return lowStockProducts; }
    public void setLowStockProducts(long v) { this.lowStockProducts = v; }
    public long getPendingReturns() { return pendingReturns; }
    public void setPendingReturns(long v) { this.pendingReturns = v; }
    public double getAverageProductRating() { return averageProductRating; }
    public void setAverageProductRating(double v) { this.averageProductRating = v; }
}
