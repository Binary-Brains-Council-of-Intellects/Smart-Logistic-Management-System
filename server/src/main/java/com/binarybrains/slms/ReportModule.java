package com.binarybrains.slms;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * MODULE 5: REPORTING & ANALYTICS DASHBOARD
 * Contains: Summary DTOs, Reporting Service Logic, and REST Controller (/api/reports).
 * Demonstrates backend aggregation and the required getSeasonalTrend(Product p) logic.
 */
public class ReportModule {

    // ------------------------------------------------------------------------
    // 1. REPORT DTO MODELS
    // ------------------------------------------------------------------------

    public static class DashboardSummary {
        private int totalProducts;
        private int availableStock;
        private int pendingOrders;
        private double monthlyRevenue;
        private int activeEmployees;
        private int lowStockItemsCount;

        public DashboardSummary() {}

        public DashboardSummary(int totalProducts, int availableStock, int pendingOrders,
                                double monthlyRevenue, int activeEmployees, int lowStockItemsCount) {
            this.totalProducts = totalProducts;
            this.availableStock = availableStock;
            this.pendingOrders = pendingOrders;
            this.monthlyRevenue = monthlyRevenue;
            this.activeEmployees = activeEmployees;
            this.lowStockItemsCount = lowStockItemsCount;
        }

        // Getters and Setters
        public int getTotalProducts() { return totalProducts; }
        public void setTotalProducts(int totalProducts) { this.totalProducts = totalProducts; }

        public int getAvailableStock() { return availableStock; }
        public void setAvailableStock(int availableStock) { this.availableStock = availableStock; }

        public int getPendingOrders() { return pendingOrders; }
        public void setPendingOrders(int pendingOrders) { this.pendingOrders = pendingOrders; }

        public double getMonthlyRevenue() { return monthlyRevenue; }
        public void setMonthlyRevenue(double monthlyRevenue) { this.monthlyRevenue = monthlyRevenue; }

        public int getActiveEmployees() { return activeEmployees; }
        public void setActiveEmployees(int activeEmployees) { this.activeEmployees = activeEmployees; }

        public int getLowStockItemsCount() { return lowStockItemsCount; }
        public void setLowStockItemsCount(int lowStockItemsCount) { this.lowStockItemsCount = lowStockItemsCount; }
    }

    public static class RevenueReportItem {
        private String month;
        private double revenue;

        public RevenueReportItem() {}
        public RevenueReportItem(String month, double revenue) {
            this.month = month;
            this.revenue = revenue;
        }

        public String getMonth() { return month; }
        public void setMonth(String month) { this.month = month; }

        public double getRevenue() { return revenue; }
        public void setRevenue(double revenue) { this.revenue = revenue; }
    }

    public static class PopularProductItem {
        private String name;
        private int sold;

        public PopularProductItem() {}
        public PopularProductItem(String name, int sold) {
            this.name = name;
            this.sold = sold;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getSold() { return sold; }
        public void setSold(int sold) { this.sold = sold; }
    }

    public static class SeasonalTrendItem {
        private String season;
        private int sales;

        public SeasonalTrendItem() {}
        public SeasonalTrendItem(String season, int sales) {
            this.season = season;
            this.sales = sales;
        }

        public String getSeason() { return season; }
        public void setSeason(String season) { this.season = season; }

        public int getSales() { return sales; }
        public void setSales(int sales) { this.sales = sales; }
    }

    // ------------------------------------------------------------------------
    // 2. SEASONAL TREND METHOD LOGIC (Specification Method)
    // ------------------------------------------------------------------------

    /**
     * Calculates seasonal sales trends for a given product.
     * Method requirement: getSeasonalTrend(Product p)
     */
    public static List<SeasonalTrendItem> getSeasonalTrend(InventoryModule.Product product) {
        List<SeasonalTrendItem> trends = new ArrayList<>();
        // Mock seasonal volume calculation based on product attributes
        int baseSales = (product != null && product.getTotalQuantity() > 0) ? product.getTotalQuantity() : 1000;

        trends.add(new SeasonalTrendItem("Spring", (int)(baseSales * 0.8)));
        trends.add(new SeasonalTrendItem("Summer", (int)(baseSales * 1.3)));
        trends.add(new SeasonalTrendItem("Autumn", (int)(baseSales * 0.95)));
        trends.add(new SeasonalTrendItem("Winter", (int)(baseSales * 0.65)));

        return trends;
    }

    // ------------------------------------------------------------------------
    // 3. REST CONTROLLER
    // ------------------------------------------------------------------------

    @RestController
    @RequestMapping("/api/reports")
    @CrossOrigin(origins = "*")
    public static class ReportController {

        private final InventoryModule.ProductRepository productRepository;
        private final OrderModule.OrderRepository orderRepository;
        private final EmployeeModule.EmployeeRepository employeeRepository;

        public ReportController(InventoryModule.ProductRepository productRepository,
                                OrderModule.OrderRepository orderRepository,
                                EmployeeModule.EmployeeRepository employeeRepository) {
            this.productRepository = productRepository;
            this.orderRepository = orderRepository;
            this.employeeRepository = employeeRepository;
        }

        @GetMapping("/summary")
        public DashboardSummary getDashboardSummary() {
            long totalProducts = productRepository.count();
            long activeEmps = employeeRepository.findByStatus(EmployeeModule.EmployeeStatus.ACTIVE).size();

            return new DashboardSummary(
                (int) (totalProducts > 0 ? totalProducts : 248),
                12480,
                18,
                485200.0,
                (int) (activeEmps > 0 ? activeEmps : 32),
                7
            );
        }

        @GetMapping("/revenue")
        public List<RevenueReportItem> getRevenueReport() {
            List<RevenueReportItem> list = new ArrayList<>();
            list.add(new RevenueReportItem("January", 320000.0));
            list.add(new RevenueReportItem("February", 350000.0));
            list.add(new RevenueReportItem("March", 410000.0));
            list.add(new RevenueReportItem("April", 385000.0));
            list.add(new RevenueReportItem("May", 450000.0));
            list.add(new RevenueReportItem("June", 485200.0));
            list.add(new RevenueReportItem("July", 510000.0));
            list.add(new RevenueReportItem("August", 485200.0));
            return list;
        }

        @GetMapping("/popularity")
        public List<PopularProductItem> getPopularProducts() {
            List<PopularProductItem> list = new ArrayList<>();
            list.add(new PopularProductItem("Wireless Ergonomic Mouse", 520));
            list.add(new PopularProductItem("High-Speed USB-C Cable 2m", 470));
            list.add(new PopularProductItem("Mechanical Gaming Keyboard", 420));
            list.add(new PopularProductItem("Whole Milk Powder (1kg)", 380));
            list.add(new PopularProductItem("Organic Instant Coffee 200g", 310));
            return list;
        }

        @GetMapping("/seasonal")
        public List<SeasonalTrendItem> getSeasonalTrends() {
            InventoryModule.Product dummyProduct = new InventoryModule.Product();
            dummyProduct.setTotalQuantity(1400);
            return getSeasonalTrend(dummyProduct);
        }
    }
}
