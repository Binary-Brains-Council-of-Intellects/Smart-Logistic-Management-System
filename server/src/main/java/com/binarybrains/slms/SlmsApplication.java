package com.binarybrains.slms;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;

/**
 * Smart Logistics Management System (SLMS) Application Server
 */
public class SlmsApplication {

    private static final int DEFAULT_PORT = 8080;

    // Encapsulation
    public static void main(String[] args) {
        try {
            String envPort = System.getenv("PORT");
            int port = (envPort != null && !envPort.trim().isEmpty()) ? Integer.parseInt(envPort.trim()) : DEFAULT_PORT;

            // Abstraction
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

            // Composition
            InventoryModule.ProductRepository productRepository = new InventoryModule.InMemoryProductRepository();
            EmployeeModule.EmployeeRepository employeeRepository = new EmployeeModule.InMemoryEmployeeRepository();
            EmployeeModule.AttendanceRepository attendanceRepository = new EmployeeModule.InMemoryAttendanceRepository();
            OrderModule.OrderRepository orderRepository = new OrderModule.InMemoryOrderRepository();
            ReviewModule.ReviewRepository reviewRepository = new ReviewModule.InMemoryReviewRepository();
            ReviewModule.ReturnRepository returnRepository = new ReviewModule.InMemoryReturnRepository();

            // Root / Health check handler for Render health checks
            server.createContext("/", exchange -> {
                String response = "{\"status\":\"UP\",\"message\":\"Smart Logistics Management System API is running\"}";
                byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.sendResponseHeaders(200, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            });

            // Composition & Polymorphism
            server.createContext("/api/products", new InventoryModule.ProductHttpHandler(productRepository));
            server.createContext("/api/employees", new EmployeeModule.EmployeeHttpHandler(employeeRepository));
            server.createContext("/api/attendance", new EmployeeModule.AttendanceHttpHandler(attendanceRepository));
            server.createContext("/api/payroll", new EmployeeModule.PayrollHttpHandler(employeeRepository, attendanceRepository));
            server.createContext("/api/orders", new OrderModule.OrderHttpHandler(orderRepository));
            server.createContext("/api/reports", new ReportModule.ReportHttpHandler(productRepository, orderRepository, employeeRepository));
            server.createContext("/api/reviews", new ReviewModule.ReviewHttpHandler(reviewRepository));
            server.createContext("/api/exchanges", new ReviewModule.ReturnHttpHandler(returnRepository));

            // Encapsulation
            server.setExecutor(Executors.newFixedThreadPool(10));

            System.out.println("==================================================================");
            System.out.println("   Smart Logistics Management System (SLMS) - Pure Raw Java");
            System.out.println("   HTTP Server started successfully on port " + port);
            System.out.println("==================================================================");

            server.start();

        } catch (IOException e) {
            System.err.println("Failed to start Raw Java HTTP Server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------------------------------------------------------
    // JSON HELPER METHODS (INTERNAL RAW JAVA SERIALIZATION & PARSING)
    // ------------------------------------------------------------------------

    // Encapsulation
    public static String toJson(Object obj) {
        if (obj == null) return "null";

        if (obj instanceof String) {
            return "\"" + escapeJson((String) obj) + "\"";
        }
        if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        }
        if (obj instanceof Enum<?>) {
            return "\"" + ((Enum<?>) obj).name() + "\"";
        }
        if (obj instanceof Collection<?>) {
            StringBuilder sb = new StringBuilder("[");
            Iterator<?> it = ((Collection<?>) obj).iterator();
            while (it.hasNext()) {
                sb.append(toJson(it.next()));
                if (it.hasNext()) sb.append(",");
            }
            sb.append("]");
            return sb.toString();
        }
        if (obj instanceof Map<?, ?>) {
            StringBuilder sb = new StringBuilder("{");
            Iterator<? extends Map.Entry<?, ?>> it = ((Map<?, ?>) obj).entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<?, ?> entry = it.next();
                sb.append("\"").append(escapeJson(String.valueOf(entry.getKey()))).append("\":");
                sb.append(toJson(entry.getValue()));
                if (it.hasNext()) sb.append(",");
            }
            sb.append("}");
            return sb.toString();
        }

        StringBuilder sb = new StringBuilder("{");
        List<Field> fields = getAllFields(obj.getClass());
        boolean first = true;

        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object val = field.get(obj);
                if (!first) sb.append(",");
                sb.append("\"").append(field.getName()).append("\":").append(toJson(val));
                first = false;
            } catch (IllegalAccessException ignored) {}
        }
        sb.append("}");
        return sb.toString();
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            fields.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }
        return fields;
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Encapsulation
    public static Map<String, String> parseJsonMap(String json) {
        Map<String, String> map = new LinkedHashMap<>();
        if (json == null || json.trim().isEmpty()) return map;

        String content = json.trim();
        if (content.startsWith("{")) content = content.substring(1);
        if (content.endsWith("}")) content = content.substring(0, content.length() - 1);

        String[] pairs = content.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        for (String pair : pairs) {
            String[] kv = pair.split(":(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", 2);
            if (kv.length == 2) {
                String key = kv[0].trim().replaceAll("^\"|\"$", "");
                String val = kv[1].trim().replaceAll("^\"|\"$", "");
                map.put(key, val);
            }
        }
        return map;
    }
}
