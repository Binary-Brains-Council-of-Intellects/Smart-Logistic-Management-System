import React, { createContext, useContext, useState } from 'react';
import {
  initialProducts,
  initialOrders,
  initialEmployees,
  initialAttendance,
  initialReviews,
  initialExchanges,
  mockMonthlyRevenue,
  mockPopularProducts,
  mockSeasonalTrends
} from '../data/mockData';

const SLMSContext = createContext();

export const SLMSProvider = ({ children }) => {
  const [user, setUser] = useState({
    isAuthenticated: true,
    username: 'Admin User',
    role: 'Warehouse Operations Manager'
  });

  const [products, setProducts] = useState(initialProducts);
  const [orders, setOrders] = useState(initialOrders);
  const [employees, setEmployees] = useState(initialEmployees);
  const [attendance, setAttendance] = useState(initialAttendance);
  const [reviews, setReviews] = useState(initialReviews);
  const [exchanges, setExchanges] = useState(initialExchanges);

  // Authentication Mock
  const login = (username, password) => {
    if (username && password) {
      setUser({
        isAuthenticated: true,
        username: username || 'Admin User',
        role: 'Warehouse Operations Manager'
      });
      return true;
    }
    return false;
  };

  const logout = () => {
    setUser({
      isAuthenticated: false,
      username: '',
      role: ''
    });
  };

  // Product Operations
  const addProduct = (newProduct) => {
    const productWithId = {
      ...newProduct,
      id: newProduct.id || `PRD-${Math.floor(1000 + Math.random() * 9000)}`,
      availableQuantity: Number(newProduct.availableQuantity || 0),
      totalQuantity: Number(newProduct.totalQuantity || 0),
      expiredQuantity: Number(newProduct.expiredQuantity || 0),
      costPrice: Number(newProduct.costPrice || 0),
      sellingPrice: Number(newProduct.sellingPrice || 0)
    };
    setProducts((prev) => [productWithId, ...prev]);
    return productWithId;
  };

  const updateProduct = (updatedProduct) => {
    setProducts((prev) =>
      prev.map((item) => (item.id === updatedProduct.id ? updatedProduct : item))
    );
  };

  const deleteProduct = (id) => {
    setProducts((prev) => prev.filter((item) => item.id !== id));
  };

  // Order Operations
  const createOrder = (orderData) => {
    const newOrder = {
      ...orderData,
      id: `ORD-${Math.floor(9000 + Math.random() * 1000)}`,
      orderDate: orderData.orderDate || new Date().toISOString().split('T')[0],
      status: 'PENDING',
      totalAmount: orderData.items.reduce((sum, i) => sum + i.subtotal, 0)
    };
    setOrders((prev) => [newOrder, ...prev]);
    return newOrder;
  };

  const updateOrderStatus = (id, newStatus) => {
    setOrders((prev) =>
      prev.map((order) => {
        if (order.id === id) {
          return { ...order, status: newStatus };
        }
        return order;
      })
    );
  };

  // Employee Operations
  const addEmployee = (empData) => {
    const newEmp = {
      ...empData,
      id: empData.id || `EMP-${Math.floor(200 + Math.random() * 800)}`,
      hourlyRate: Number(empData.hourlyRate || 0),
      status: empData.status || 'ACTIVE'
    };
    setEmployees((prev) => [newEmp, ...prev]);
  };

  const updateEmployee = (updatedEmp) => {
    setEmployees((prev) =>
      prev.map((emp) => (emp.id === updatedEmp.id ? updatedEmp : emp))
    );
  };

  const deleteEmployee = (id) => {
    setEmployees((prev) => prev.filter((emp) => emp.id !== id));
  };

  // Attendance Operations
  const addAttendanceRecord = (attData) => {
    const newAtt = {
      ...attData,
      id: `ATT-${Math.floor(500 + Math.random() * 500)}`,
      hoursWorked: Number(attData.hoursWorked || 0)
    };
    setAttendance((prev) => [newAtt, ...prev]);
  };

  // Review Operations
  const addReview = (revData) => {
    const newRev = {
      ...revData,
      id: `REV-${Math.floor(300 + Math.random() * 700)}`,
      rating: Number(revData.rating || 5),
      reviewDate: revData.reviewDate || new Date().toISOString().split('T')[0]
    };
    setReviews((prev) => [newRev, ...prev]);
  };

  // Exchange Operations
  const addExchange = (excData) => {
    const newExc = {
      ...excData,
      id: `EXC-${Math.floor(400 + Math.random() * 600)}`,
      exchangeDate: excData.exchangeDate || new Date().toISOString().split('T')[0],
      status: 'Pending Inspection'
    };
    setExchanges((prev) => [newExc, ...prev]);
  };

  // Aggregated Dashboard Metrics
  const totalProductsCount = products.length;
  const availableStockCount = products.reduce((acc, p) => acc + Number(p.availableQuantity), 0);
  const pendingOrdersCount = orders.filter((o) => o.status === 'PENDING').length;
  const monthlyRevenueTotal = 485200;
  const activeEmployeesCount = employees.filter((e) => e.status === 'ACTIVE').length;
  const lowStockItems = products.filter((p) => Number(p.availableQuantity) <= 10);

  return (
    <SLMSContext.Provider
      value={{
        user,
        login,
        logout,
        products,
        addProduct,
        updateProduct,
        deleteProduct,
        orders,
        createOrder,
        updateOrderStatus,
        employees,
        addEmployee,
        updateEmployee,
        deleteEmployee,
        attendance,
        addAttendanceRecord,
        reviews,
        addReview,
        exchanges,
        addExchange,
        mockMonthlyRevenue,
        mockPopularProducts,
        mockSeasonalTrends,
        totalProductsCount,
        availableStockCount,
        pendingOrdersCount,
        monthlyRevenueTotal,
        activeEmployeesCount,
        lowStockItems
      }}
    >
      {children}
    </SLMSContext.Provider>
  );
};

export const useSLMS = () => {
  const context = useContext(SLMSContext);
  if (!context) {
    throw new Error('useSLMS must be used within an SLMSProvider');
  }
  return context;
};
