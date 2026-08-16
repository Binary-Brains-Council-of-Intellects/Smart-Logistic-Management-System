package com.binarybrains.slms;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Reporting & Analytics Dashboard Module
 */
public class ReportModule {

    // Abstraction
    public static abstract class BaseReportItem {
        // Encapsulation
        private String reportCategory;

        public BaseReportItem() {}
        public BaseReportItem(String reportCategory) {
            this.reportCategory = reportCategory;
        }

        // Encapsulation
        public String getReportCategory() { return reportCategory; }
        public void setReportCategory(String reportCategory) { this.reportCategory = reportCategory; }

        // Abstraction
        public abstract String getFormattedSummary();
    }

    // Encapsulation
    public static class DashboardSummary {

        // Encapsulation
        private int totalProducts;
        private int availableStock;
        private int pendingOrders;
        private double monthlyRevenue;
        private int activeEmployees;
        private int lowStockItemsCount;

        // Polymorphism
        public DashboardSummary() {}

        // Polymorphism
        public DashboardSummary(int totalProducts, int availableStock, int pendingOrders,
                                double monthlyRevenue, int activeEmployees, int lowStockItemsCount) {
            this.totalProducts = totalProducts;
            this.availableStock = availableStock;
            this.pendingOrders = pendingOrders;
            this.monthlyRevenue = monthlyRevenue;
            this.activeEmployees = activeEmployees;
            this.lowStockItemsCount = lowStockItemsCount;
        }

        // Encapsulation
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

    // Inheritance
    public static class RevenueReportItem extends BaseReportItem {

        // Encapsulation
        private String month;
        private double revenue;

        // Polymorphism
        public RevenueReportItem() { super("REVENUE"); }

        // Polymorphism
        public RevenueReportItem(String month, double revenue) {
            super("REVENUE");
            this.month = month;
            this.revenue = revenue;
        }

        // Polymorphism
        @Override
        public String getFormattedSummary() {
            return "Month: " + month + " | Revenue: $" + revenue;
        }

        // Encapsulation
        public String getMonth() { return month; }
        public void setMonth(String month) { this.month = month; }

        public double getRevenue() { return revenue; }
        public void setRevenue(double revenue) { this.revenue = revenue; }
    }

    // Inheritance
    public static class PopularProductItem extends BaseReportItem {

        // Encapsulation
        private String name;
        private int sold;

        // Polymorphism
        public PopularProductItem() { super("POPULARITY"); }

        // Polymorphism
        public PopularProductItem(String name, int sold) {
            super("POPULARITY");
            this.name = name;
            this.sold = sold;
        }

        // Polymorphism
        @Override
        public String getFormattedSummary() {
            return "Product: " + name + " | Total Sold: " + sold;
        }

        // Encapsulation
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getSold() { return sold; }
        public void setSold(int sold) { this.sold = sold; }
    }

    // Inheritance
    public static class SeasonalTrendItem extends BaseReportItem {

        // Encapsulation
        private String season;
        private int sales;

        // Polymorphism
        public SeasonalTrendItem() { super("SEASONAL"); }

        // Polymorphism
        public SeasonalTrendItem(String season, int sales) {
            super("SEASONAL");
            this.season = season;
            this.sales = sales;
        }

        // Polymorphism
        @Override
        public String getFormattedSummary() {
            return "Season: " + season + " | Projected Sales: " + sales;
        }

        // Encapsulation
        public String getSeason() { return season; }
        public void setSeason(String season) { this.season = season; }

        public int getSales() { return sales; }
        public void setSales(int sales) { this.sales = sales; }
    }

    // Polymorphism
    public static List<SeasonalTrendItem> getSeasonalTrend(InventoryModule.Product product) {
        return getSeasonalTrend(product, 1.0);
    }

    // Polymorphism
    public static List<SeasonalTrendItem> getSeasonalTrend(InventoryModule.Product product, double customDemandMultiplier) {
        List<SeasonalTrendItem> trends = new ArrayList<>();
        int baseSales = (product != null && product.getTotalQuantity() > 0) ? product.getTotalQuantity() : 1000;

        trends.add(new SeasonalTrendItem("Spring", (int)(baseSales * 0.8 * customDemandMultiplier)));
        trends.add(new SeasonalTrendItem("Summer", (int)(baseSales * 1.3 * customDemandMultiplier)));
        trends.add(new SeasonalTrendItem("Autumn", (int)(baseSales * 0.95 * customDemandMultiplier)));
        trends.add(new SeasonalTrendItem("Winter", (int)(baseSales * 0.65 * customDemandMultiplier)));

        return trends;
    }

    // Composition
    public static class ReportHttpHandler implements HttpHandler {

        // Composition
        private final InventoryModule.ProductRepository productRepository;
        private final OrderModule.OrderRepository orderRepository;
        private final EmployeeModule.EmployeeRepository employeeRepository;

        public ReportHttpHandler(InventoryModule.ProductRepository productRepository,
                                 OrderModule.OrderRepository orderRepository,
                                 EmployeeModule.EmployeeRepository employeeRepository) {
            this.productRepository = productRepository;
            this.orderRepository = orderRepository;
            this.employeeRepository = employeeRepository;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) { exchange.sendResponseHeaders(204, -1); return; }

            String path = exchange.getRequestURI().getPath();

            if (path.endsWith("/summary")) {
                long totalProducts = productRepository.count();
                long activeEmps = employeeRepository.findByStatus(EmployeeModule.EmployeeStatus.ACTIVE).size();
                DashboardSummary summary = new DashboardSummary(
                    (int) (totalProducts > 0 ? totalProducts : 248),
                    12480, 18, 485200.0,
                    (int) (activeEmps > 0 ? activeEmps : 32), 7
                );
                sendJsonResponse(exchange, 200, SlmsApplication.toJson(summary));
            } else if (path.endsWith("/revenue")) {
                List<RevenueReportItem> list = new ArrayList<>();
                list.add(new RevenueReportItem("January", 320000.0));
                list.add(new RevenueReportItem("February", 350000.0));
                list.add(new RevenueReportItem("March", 410000.0));
                list.add(new RevenueReportItem("April", 385000.0));
                list.add(new RevenueReportItem("May", 450000.0));
                list.add(new RevenueReportItem("June", 485200.0));
                list.add(new RevenueReportItem("July", 510000.0));
                list.add(new RevenueReportItem("August", 485200.0));
                sendJsonResponse(exchange, 200, SlmsApplication.toJson(list));
            } else if (path.endsWith("/popularity")) {
                List<PopularProductItem> list = new ArrayList<>();
                list.add(new PopularProductItem("Wireless Ergonomic Mouse", 520));
                list.add(new PopularProductItem("High-Speed USB-C Cable 2m", 470));
                list.add(new PopularProductItem("Mechanical Gaming Keyboard", 420));
                list.add(new PopularProductItem("Whole Milk Powder (1kg)", 380));
                list.add(new PopularProductItem("Organic Instant Coffee 200g", 310));
                sendJsonResponse(exchange, 200, SlmsApplication.toJson(list));
            } else if (path.endsWith("/seasonal")) {
                InventoryModule.Product dummyProduct = new InventoryModule.Product();
                dummyProduct.setTotalQuantity(1400);
                sendJsonResponse(exchange, 200, SlmsApplication.toJson(getSeasonalTrend(dummyProduct)));
            } else {
                sendJsonResponse(exchange, 404, "{\"error\":\"Report endpoint not found\"}");
            }
        }

        private void sendJsonResponse(HttpExchange exchange, int statusCode, String json) throws IOException {
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }
}
