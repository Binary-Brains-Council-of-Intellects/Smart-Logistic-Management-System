# Smart Logistic Management System

A comprehensive full-stack logistics management application built with **React** and **Java**, featuring employee management, inventory tracking, order processing, product management, and advanced reporting capabilities.

<div align="center">

![Java](https://img.shields.io/badge/Java-17-ED8936?style=flat-square&logo=openjdk)
![React](https://img.shields.io/badge/React-19-61DAFB?style=flat-square&logo=react)
![Vite](https://img.shields.io/badge/Vite-5.0-646CFF?style=flat-square&logo=vite)
![Tailwind CSS](https://img.shields.io/badge/Tailwind%20CSS-4.3-06B6D4?style=flat-square&logo=tailwind-css)
![License](https://img.shields.io/badge/License-MIT-green.svg?style=flat-square)

</div>

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Running the Application](#running-the-application)
- [Project Structure](#project-structure)
- [Key Modules](#key-modules)
- [API Endpoints](#api-endpoints)
- [Development Workflow](#development-workflow)
- [Contributing](#contributing)

## 🎯 Overview

Smart Logistic Management System (SLMS) is a university Object-Oriented Programming (CSE222) project designed to demonstrate enterprise-level application development principles. The system provides a unified platform for managing logistics operations, including employee administration, product inventory, order management, customer reviews, and comprehensive business analytics.

Built with **zero framework dependencies** on the backend for maximum learning value, the system showcases pure Java design patterns and object-oriented principles while leveraging modern frontend technologies for an intuitive user interface.

## ✨ Features

### 👥 Employee Management
- **Employee Directory**: Create, read, update, and delete employee records
- **Attendance Tracking**: Track employee attendance with date and status
- **Payroll Management**: View and manage employee payroll information
- **Department Organization**: Organize employees by department

### 📦 Inventory Management
- **Product Catalog**: Comprehensive product management with details and pricing
- **Stock Tracking**: Real-time inventory level monitoring
- **Product Categories**: Organize products into logical categories
- **Stock Alerts**: Monitor low-stock situations

### 🛒 Order Management
- **Order Creation**: Create and manage customer orders
- **Order Tracking**: Track order status from placement to delivery
- **Order History**: Complete order history with detailed information
- **Order Analytics**: Insights into order patterns and trends

### ⭐ Review & Rating System
- **Product Reviews**: Customers can leave detailed product reviews
- **Rating System**: 5-star rating system for products
- **Exchange Tracking**: Manage product exchanges and returns
- **Customer Feedback**: Aggregate and analyze customer feedback

### 📊 Advanced Reporting & Analytics
- **Revenue Charts**: Track revenue trends over time
- **Product Popularity**: Analyze best-selling products
- **Seasonal Trends**: Identify seasonal patterns in sales
- **Rating Analysis**: Visualize product ratings distribution
- **Exchange Analysis**: Track product exchanges and trends
- **Custom Reports**: Generate detailed business reports

### 🎨 User Interface
- **Responsive Design**: Works seamlessly on desktop, tablet, and mobile
- **Real-time Charts**: Interactive visualizations using Chart.js
- **Modal Forms**: Intuitive form modals for data entry
- **Status Indicators**: Clear status badges for orders and activities
- **Loading States**: Smooth loading animations and empty states

## 🛠 Tech Stack

### Frontend
- **React 19**: Modern UI library with hooks and concurrent features
- **Vite 5**: Lightning-fast build tool and dev server
- **Tailwind CSS 4**: Utility-first CSS framework
- **DaisyUI 5**: Pre-built components for Tailwind CSS
- **Chart.js 4**: Data visualization library
- **React Router DOM 7**: Client-side routing
- **Lucide React**: Beautiful, consistent icon library
- **ESLint**: Code quality and style enforcement

### Backend
- **Java 17**: LTS version with modern features and APIs
- **Maven**: Build automation and dependency management
- **Docker**: Containerization for deployment
- **Raw Java Architecture**: No external framework dependencies for core logic

### Project Structure
```
Smart-Logistic-Management-System/
├── client/                          # React frontend application
│   ├── src/
│   │   ├── components/              # React components
│   │   │   ├── employees/           # Employee-related components
│   │   │   ├── products/            # Product-related components
│   │   │   ├── orders/              # Order-related components
│   │   │   ├── reviews/             # Review/Exchange components
│   │   │   ├── reports/             # Analytics & charts
│   │   │   ├── common/              # Shared components
│   │   │   └── layout/              # Layout components
│   │   ├── pages/                   # Page components
│   │   ├── services/                # API service layer
│   │   ├── context/                 # React context for state
│   │   ├── data/                    # Mock data
│   │   ├── router/                  # Routing configuration
│   │   ├── App.jsx                  # Root component
│   │   └── main.jsx                 # Entry point
│   ├── vite.config.js               # Vite configuration
│   ├── package.json                 # Dependencies
│   └── netlify.toml                 # Deployment config
│
└── server/                          # Java backend application
    ├── src/main/java/com/binarybrains/slms/
    │   ├── EmployeeModule.java      # Employee management
    │   ├── InventoryModule.java     # Product inventory
    │   ├── OrderModule.java         # Order processing
    │   ├── ReviewModule.java        # Reviews & ratings
    │   ├── ReportModule.java        # Analytics & reporting
    │   └── SlmsApplication.java     # Application entry point
    ├── src/main/resources/
    │   └── application.yml          # Server configuration
    ├── pom.xml                      # Maven dependencies
    ├── Dockerfile                   # Docker image definition
    └── mvnw.cmd                     # Maven wrapper (Windows)
```

## 🏗 Architecture

### Frontend Architecture
- **Component-Based**: Modular React components for maintainability
- **Context API**: Global state management using React Context
- **Service Layer**: Centralized API communication via `apiService.js`
- **Responsive Layout**: Mobile-first design approach with Tailwind CSS
- **Separation of Concerns**: Pages, components, services, and contexts clearly separated

### Backend Architecture
- **Module-Based Design**: Separate modules for each business domain
- **OOP Principles**: Demonstrates inheritance, polymorphism, and encapsulation
- **Service Layer Pattern**: Business logic separated from data access
- **Configuration Management**: Externalized configuration via `application.yml`
- **Docker Support**: Production-ready containerization

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

### For Frontend Development
- **Node.js**: v18+ (includes npm)
- **npm**: v9+ (comes with Node.js)

### For Backend Development
- **Java Development Kit (JDK)**: v17 or higher
- **Maven**: v3.6+ (or use the included `mvnw.cmd`)

### For Full Stack Development
- **Git**: For version control
- **Docker**: (Optional) For containerized deployment

### Verify Installation
```bash
# Check Node.js and npm
node --version
npm --version

# Check Java
java -version
javac -version

# Check Maven
mvn --version
```

## 💻 Installation & Setup

### Frontend Setup

1. **Navigate to the client directory**
   ```bash
   cd client
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Configure API endpoint** (if needed)
   - Update `src/services/apiService.js` with your backend URL
   - Default: `http://localhost:8080/api`

### Backend Setup

1. **Navigate to the server directory**
   ```bash
   cd server
   ```

2. **Build the project**
   ```bash
   # Using Maven directly (requires Maven installed)
   mvn clean install

   # Or using Maven wrapper (Windows)
   mvnw.cmd clean install
   ```

3. **Configure the application**
   - Edit `src/main/resources/application.yml` as needed
   - Set port, database, and other configurations

### Docker Setup (Optional)

1. **Build Docker image**
   ```bash
   cd server
   docker build -t slms-server:latest .
   ```

2. **Run Docker container**
   ```bash
   docker run -d -p 8080:8080 --name slms-server slms-server:latest
   ```

## 🚀 Running the Application

### Option 1: Development Mode (Recommended for Development)

**Terminal 1 - Start Backend**
```bash
cd server
mvn exec:java -Dexec.mainClass="com.binarybrains.slms.SlmsApplication"

# Or using Maven wrapper
mvnw.cmd exec:java -Dexec.mainClass="com.binarybrains.slms.SlmsApplication"
```

**Terminal 2 - Start Frontend**
```bash
cd client
npm run dev
```

The application will be available at:
- **Frontend**: http://localhost:5173
- **Backend API**: http://localhost:8080/api

### Option 2: Production Build

**Build Frontend**
```bash
cd client
npm run build
npm run preview
```

**Build Backend**
```bash
cd server
mvn package
java -jar target/slms-*.jar
```

### Option 3: Docker Deployment

```bash
# Build backend container
cd server
docker build -t slms-server:latest .

# Run container
docker run -d -p 8080:8080 --name slms-server slms-server:latest

# Start frontend
cd ../client
npm run build
npm run preview
```

## 📁 Project Structure

### Frontend Components Breakdown

```
src/components/
├── common/                      # Shared UI components
│   ├── EmptyState.jsx          # Empty data state component
│   ├── LoadingState.jsx        # Loading spinner component
│   ├── Modal.jsx               # Reusable modal wrapper
│   ├── PageHeader.jsx          # Page title and header
│   ├── StatCard.jsx            # Statistics card component
│   └── StatusBadge.jsx         # Status indicator component
│
├── employees/                  # Employee management
│   ├── EmployeeFormModal.jsx   # Add/Edit employee form
│   ├── EmployeeTable.jsx       # Employee list table
│   ├── AttendanceFormModal.jsx # Attendance tracking
│   └── PayrollTable.jsx        # Payroll management
│
├── products/                   # Product management
│   ├── ProductFormModal.jsx    # Add/Edit product form
│   ├── ProductTable.jsx        # Product list table
│   └── ProductDetailsModal.jsx # Product details view
│
├── orders/                     # Order management
│   ├── OrderFormModal.jsx      # Create/Edit order form
│   ├── OrderTable.jsx          # Order list table
│   └── OrderDetailsModal.jsx   # Order details view
│
├── reviews/                    # Review management
│   ├── ReviewFormModal.jsx     # Add review form
│   ├── ReviewTable.jsx         # Reviews list table
│   ├── ExchangeFormModal.jsx   # Exchange request form
│   └── ExchangeTable.jsx       # Exchanges list table
│
└── reports/                    # Analytics & visualizations
    ├── RevenueChart.jsx        # Revenue trend chart
    ├── ProductPopularityChart.jsx # Product sales chart
    ├── RatingChart.jsx         # Rating distribution
    ├── SeasonalTrendChart.jsx  # Seasonal analysis
    └── ExchangeChart.jsx       # Exchange analysis
```

### Backend Modules Breakdown

```
src/main/java/com/binarybrains/slms/
├── EmployeeModule.java         # Employee CRUD & management
├── InventoryModule.java        # Product & stock management
├── OrderModule.java            # Order processing logic
├── ReviewModule.java           # Review & rating system
├── ReportModule.java           # Analytics & reporting
└── SlmsApplication.java        # Application entry point
```

## 🎯 Key Modules

### EmployeeModule
Manages all employee-related operations including:
- Employee record creation and management
- Attendance tracking
- Payroll calculations
- Department assignments

### InventoryModule
Handles product inventory and stock management:
- Product catalog management
- Stock level tracking
- Inventory updates
- Stock alerts

### OrderModule
Processes customer orders and tracking:
- Order creation and management
- Order status tracking
- Order history and details
- Order fulfillment

### ReviewModule
Manages customer reviews and ratings:
- Product reviews and ratings
- Customer feedback collection
- Exchange and return requests
- Rating aggregation

### ReportModule
Provides business analytics and insights:
- Revenue analysis
- Product popularity metrics
- Seasonal trend analysis
- Rating distribution
- Exchange tracking

## 🔌 API Endpoints

The backend exposes RESTful API endpoints for all operations:

```
Base URL: http://localhost:8080/api

Employees:
GET    /api/employees              # Get all employees
POST   /api/employees              # Create employee
GET    /api/employees/:id          # Get employee details
PUT    /api/employees/:id          # Update employee
DELETE /api/employees/:id          # Delete employee

Products:
GET    /api/products               # Get all products
POST   /api/products               # Create product
GET    /api/products/:id           # Get product details
PUT    /api/products/:id           # Update product
DELETE /api/products/:id           # Delete product

Orders:
GET    /api/orders                 # Get all orders
POST   /api/orders                 # Create order
GET    /api/orders/:id             # Get order details
PUT    /api/orders/:id             # Update order
DELETE /api/orders/:id             # Delete order

Reviews:
GET    /api/reviews                # Get all reviews
POST   /api/reviews                # Create review
PUT    /api/reviews/:id            # Update review

Reports:
GET    /api/reports/revenue        # Revenue analytics
GET    /api/reports/products       # Product popularity
GET    /api/reports/ratings        # Rating analysis
GET    /api/reports/trends         # Seasonal trends
```

## 🔧 Development Workflow

### Code Style & Linting

**Frontend**
```bash
cd client

# Run ESLint
npm run lint

# Lint specific file
npx eslint src/components/MyComponent.jsx
```

### Building for Production

**Frontend**
```bash
cd client
npm run build
# Output: dist/ directory ready for deployment
```

**Backend**
```bash
cd server
mvn clean package
# Output: target/slms-*.jar file ready for deployment
```

### Hot Reload Development

**Frontend** - Vite provides automatic hot module reload:
```bash
cd client
npm run dev
# Changes are reflected instantly in the browser
```

**Backend** - Use your IDE's run/debug configurations or Maven:
```bash
cd server
mvn exec:java -Dexec.mainClass="com.binarybrains.slms.SlmsApplication"
```

## 📦 Dependencies

### Frontend Dependencies
- **react** (^19.2.8) - UI library
- **react-dom** (^19.2.8) - React rendering
- **react-router-dom** (^7.18.2) - Routing
- **tailwindcss** (^4.3.3) - Styling
- **chart.js** (^4.5.1) - Charts
- **react-chartjs-2** (^5.3.1) - React Chart wrapper
- **lucide-react** (^1.31.0) - Icons
- **daisyui** (^5.7.16) - UI components

### Development Dependencies
- **vite** (^8.2.0) - Build tool
- **@vitejs/plugin-react** (^6.0.4) - React plugin
- **eslint** (^10.8.0) - Linting
- **tailwindcss/vite** (^4.3.3) - Tailwind integration

### Backend Dependencies
- Java 17 JDK
- Maven 3.6+

## 📝 Contributing

### Getting Started with Development

1. **Fork the repository** and create a feature branch
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make your changes** and test thoroughly

3. **Follow code standards**
   - Frontend: ESLint configuration in the project
   - Backend: Java code style conventions (similar to Google Java Style)

4. **Commit your changes**
   ```bash
   git commit -m "feat: add new feature description"
   ```

5. **Push to your branch and create a Pull Request**

### Code Guidelines

- Keep components small and focused
- Use meaningful variable and function names
- Add comments for complex logic
- Follow the existing project structure
- Test your changes before submitting

## 📄 License & Credits

**Project**: Smart Logistic Management System  
**Version**: 1.0.0  
**Organization**: Binary Brains  
**Course**: CSE222 - Object Oriented Programming Lab  
**University**: L2 T2 Summer 2026  

This is a university project created for educational purposes to demonstrate:
- Object-Oriented Programming principles
- Full-stack application development
- Modern frontend and backend technologies
- Enterprise-level system design

---

<div align="center">

**Built with ❤️ by Binary Brains**

</div>
