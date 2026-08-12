package com.binarybrains.slms.report.dto;

public class InventoryReportDTO {
    private long totalProducts;
    private long activeProducts;
    private long totalStock;
    private long lowStockProducts;
    private long expiredProducts;
    private long nearExpiryProducts;
    private long perishableProducts;
    private long nonPerishableProducts;

    public long getTotalProducts() { return totalProducts; }
    public void setTotalProducts(long v) { this.totalProducts = v; }
    public long getActiveProducts() { return activeProducts; }
    public void setActiveProducts(long v) { this.activeProducts = v; }
    public long getTotalStock() { return totalStock; }
    public void setTotalStock(long v) { this.totalStock = v; }
    public long getLowStockProducts() { return lowStockProducts; }
    public void setLowStockProducts(long v) { this.lowStockProducts = v; }
    public long getExpiredProducts() { return expiredProducts; }
    public void setExpiredProducts(long v) { this.expiredProducts = v; }
    public long getNearExpiryProducts() { return nearExpiryProducts; }
    public void setNearExpiryProducts(long v) { this.nearExpiryProducts = v; }
    public long getPerishableProducts() { return perishableProducts; }
    public void setPerishableProducts(long v) { this.perishableProducts = v; }
    public long getNonPerishableProducts() { return nonPerishableProducts; }
    public void setNonPerishableProducts(long v) { this.nonPerishableProducts = v; }
}
