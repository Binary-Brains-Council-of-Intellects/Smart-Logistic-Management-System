# Smart Logistics Management System (SLMS) — Backend

A university Object-Oriented Programming (OOP) project built with **Java 17+**, **Spring Boot 3**, and **MongoDB**.

---

## Technical Stack

- **Java 17+**
- **Spring Boot 3.2** (Spring Web, Spring Data MongoDB, Jakarta Validation)
- **MongoDB** (Document persistence)
- **Maven** (Build & dependency management)
- **JUnit 5 & Mockito** (Unit & service layer testing)
- **SpringDoc OpenAPI (Swagger UI)** (API documentation)

---

## 1. Object-Oriented Programming (OOP) Mapping

This project explicitly demonstrates core OOP concepts in executable Java code:

| OOP Concept | Class / File | Technical Explanation |
| :--- | :--- | :--- |
| **Abstraction** | [`Product`](file:///server/src/main/java/com/binarybrains/slms/inventory/model/Product.java), [`Employee`](file:///server/src/main/java/com/binarybrains/slms/employee/model/Employee.java) | Abstract classes that cannot be instantiated directly. Define core template state and abstract contract methods (`calculateStorageRequirement()`, `calculatePayroll()`). |
| **Encapsulation** | All Model Classes | Domain fields are `private`. Stock modification and state transitions are strictly controlled via domain methods like `product.addStock()`, `product.dispatch()`, `returnRequest.canRestoreToInventory()`. Direct mutating setters for internal state are omitted or restricted. |
| **Inheritance** | [`PerishableProduct`](file:///server/src/main/java/com/binarybrains/slms/inventory/model/PerishableProduct.java), [`NonPerishableProduct`](file:///server/src/main/java/com/binarybrains/slms/inventory/model/NonPerishableProduct.java), [`WarehouseStaff`](file:///server/src/main/java/com/binarybrains/slms/employee/model/WarehouseStaff.java), [`DeliveryDriver`](file:///server/src/main/java/com/binarybrains/slms/employee/model/DeliveryDriver.java), [`Manager`](file:///server/src/main/java/com/binarybrains/slms/employee/model/Manager.java) | Subclasses inherit common attributes (`productId`, `sku`, `baseSalary`) from their abstract base classes and extend them with specialized state (e.g., storage temp, license number). |
| **Polymorphism** | `calculatePayroll()`, `calculateStorageRequirement()` | Overridden methods produce subclass-specific runtime behavior. In `PayrollService`, `employee.calculatePayroll()` dynamically resolves to `WarehouseStaff` (overtime-based), `DeliveryDriver` (bonus-per-delivery), or `Manager` (team bonus). |
| **Interfaces** | [`Perishable`](file:///server/src/main/java/com/binarybrains/slms/inventory/model/Perishable.java) | Interface defining expiry behavior (`isNearExpiry()`, `checkExpiry()`). Implemented by `PerishableProduct`. Enables interface-based polymorphism (`product instanceof Perishable`). |
| **Method Overloading** | [`Product`](file:///server/src/main/java/com/binarybrains/slms/inventory/model/Product.java), [`ApiResponse`](file:///server/src/main/java/com/binarybrains/slms/common/response/ApiResponse.java) | Same method name with different parameter signatures: `addStock(int)` vs `addStock(int, String batchNumber)`, `createProduct(CreateProductRequest)` vs `createProduct(ProductType, ...)`. |
| **Method Overriding** | Subclasses of `Product` and `Employee` | `@Override` annotations on subclass implementations of abstract base methods and interface contracts. |

---

## 2. Design Patterns

| Pattern | Implementation File | Explanation |
| :--- | :--- | :--- |
| **Factory Method Pattern** | [`ProductFactory`](file:///server/src/main/java/com/binarybrains/slms/inventory/factory/ProductFactory.java), [`EmployeeFactory`](file:///server/src/main/java/com/binarybrains/slms/employee/factory/EmployeeFactory.java) | Decouples object creation from controllers/services. Returns concrete subclasses (`PerishableProduct` / `NonPerishableProduct`) based on type enum parameters. |
| **Strategy Pattern** | [`PricingStrategy`](file:///server/src/main/java/com/binarybrains/slms/inventory/strategy/PricingStrategy.java), [`RegularPricingStrategy`](file:///server/src/main/java/com/binarybrains/slms/inventory/strategy/RegularPricingStrategy.java), [`WholesalePricingStrategy`](file:///server/src/main/java/com/binarybrains/slms/inventory/strategy/WholesalePricingStrategy.java), [`DiscountPricingStrategy`](file:///server/src/main/java/com/binarybrains/slms/inventory/strategy/DiscountPricingStrategy.java), [`PricingContext`](file:///server/src/main/java/com/binarybrains/slms/inventory/strategy/PricingContext.java) | Family of pricing algorithms for order calculation. `PricingContext` uses Spring DI to inject all strategies and resolves the active one at runtime based on order parameters. |
| **Singleton Pattern** | Spring Framework IoC Container | All stateless Spring components (`@Service`, `@Repository`, `@RestController`, `@Component`) operate in Spring's default Singleton scope. |

---

## 3. MongoDB Data Modeling Decisions

1. **Polymorphic Single Collection Inheritance**:
   - `products` collection stores both `PerishableProduct` and `NonPerishableProduct`.
   - `employees` collection stores `WarehouseStaff`, `DeliveryDriver`, and `Manager`.
   - Spring Data MongoDB uses the `_class` discriminator attribute to automatically rehydrate the correct concrete Java subclass upon retrieval.
2. **Embedded Documents**:
   - `Order` contains an embedded `List<OrderItem>`. Order items have no independent lifecycle outside of an order and are always read/written together.
3. **Document Referencing & Indexes**:
   - `Order`, `Attendance`, `Payroll`, `Review`, and `ReturnRequest` store foreign string IDs (`customerId`, `productId`, `employeeId`).
   - `@Indexed` and compound indexes (e.g. unique compound index on `(employee_id, date)` in `Attendance`) ensure high query performance and prevent duplicate domain state.

---

## 4. REST API Endpoint Summary

### Product & Inventory (`/api/products`)
- `POST /api/products` — Create product (Uses Factory Method)
- `GET /api/products` — List all products
- `GET /api/products/{id}` — Get product by ID
- `PUT /api/products/{id}` — Update product details
- `PATCH /api/products/{id}/add-stock` — Add inventory stock
- `PATCH /api/products/{id}/deduct-stock` — Deduct inventory stock
- `GET /api/products/expired` — List expired products
- `GET /api/products/near-expiry?days=30` — List near-expiry products
- `GET /api/products/low-stock?threshold=10` — Stock level alerts

### Employee & Workforce (`/api/employees`)
- `POST /api/employees` — Register employee (Uses Factory Method)
- `GET /api/employees` — List all employees
- `GET /api/employees/{id}` — Get employee details (shows calculated payroll)
- `PUT /api/employees/{id}` — Update employee
- `PATCH /api/employees/{id}/deactivate` — Deactivate employee

### Attendance (`/api/attendance`)
- `POST /api/attendance` — Record daily attendance (Prevents duplicates)
- `GET /api/attendance/employee/{employeeId}` — Get employee attendance history
- `GET /api/attendance/date/{date}` — Get daily attendance sheet

### Automated Payroll (`/api/payroll`)
- `POST /api/payroll` — Generate monthly payroll record (Uses Polymorphism)
- `POST /api/payroll/generate-all` — Batch payroll calculation for active workforce
- `GET /api/payroll/employee/{employeeId}` — Payroll history

### Customers (`/api/customers`)
- `POST /api/customers` — Create customer profile
- `GET /api/customers` — List active customers
- `GET /api/customers/{id}` — Get customer details

### Orders & Sales (`/api/orders`)
- `POST /api/orders` — Create order (Validates stock, checks expiry, applies Strategy pattern, deducts stock)
- `GET /api/orders/{id}` — Get order details
- `PATCH /api/orders/{id}/status` — Update order progress
- `PATCH /api/orders/{id}/cancel` — Cancel order and restore stock

### Reviews & Feedback (`/api/reviews`)
- `POST /api/reviews` — Submit product review (Rating validated 1–5)
- `GET /api/reviews/product/{productId}` — Get product reviews
- `GET /api/reviews/product/{productId}/average-rating` — Get average rating

### Returns & Exchanges (`/api/returns`)
- `POST /api/returns` — Request item return
- `PATCH /api/returns/{id}/approve` — Approve return request
- `PATCH /api/returns/{id}/complete` — Complete return (Restores inventory **only** if item is non-damaged & non-expired)

### Reporting & Analytics (`/api/reports`)
- `GET /api/reports/dashboard` — System-wide operational dashboard
- `GET /api/reports/sales` — Sales & revenue analytics
- `GET /api/reports/inventory` — Stock & expiry analytics
- `GET /api/reports/workforce` — Payroll & workforce analytics

---

## 5. How to Run

### Prerequisites
- **Java 17+**
- **MongoDB** running locally on `mongodb://localhost:27017` (or configured via environment variables)

### Environment Configuration
Copy `.env.example` to `.env` or set environment variables:
```bash
MONGODB_URI=mongodb://localhost:27017
MONGODB_DATABASE=slms_db
SPRING_PROFILES_ACTIVE=dev
```

### Build & Run
```bash
# Build project
mvn clean compile

# Run tests
mvn test

# Start application (Dev profile automatically seeds realistic test data)
mvn spring-boot:run
```

### OpenAPI / Swagger Documentation
Once running, open:
`http://localhost:8080/swagger-ui.html`
