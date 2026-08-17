import React, { createContext, useContext, useState, useEffect } from 'react';
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
import { apiService } from '../services/apiService';

const SLMSContext = createContext();

// Helper functions to normalize data from Spring Boot to match React component expectations
function normalizeProduct(p) {
  const id = p.productId || p.id;
  return {
    ...p,
    id,
    productId: id,
    availableQuantity: p.availableQuantity !== undefined ? p.availableQuantity : (p.totalQuantity || 0),
    totalQuantity: p.totalQuantity !== undefined ? p.totalQuantity : (p.availableQuantity || 0),
    costPrice: Number(p.costPrice || 0),
    sellingPrice: Number(p.sellingPrice || 0)
  };
}

function normalizeOrder(o) {
  const rawItems = Array.isArray(o.items) ? o.items : [];
  const items = rawItems.map((i) => ({
    ...i,
    productId: i.productId || i.id,
    productName: i.productName || i.name || 'Product',
    quantity: Number(i.quantity !== undefined ? i.quantity : 1),
    unitPrice: Number(i.unitPrice !== undefined ? i.unitPrice : 0),
    subtotal: Number(i.subtotal !== undefined ? i.subtotal : (Number(i.quantity || 1) * Number(i.unitPrice || 0)))
  }));

  const computedUnits = items.reduce((sum, i) => sum + i.quantity, 0);
  const itemCount = o.itemCount !== undefined && Number(o.itemCount) > 0 
    ? Number(o.itemCount) 
    : (computedUnits > 0 ? computedUnits : (rawItems.length > 0 ? rawItems.length : 0));

  return {
    ...o,
    id: o.id,
    customerName: o.customerName || 'Walk-in Customer',
    orderDate: o.orderDate ? String(o.orderDate) : new Date().toISOString().split('T')[0],
    itemCount,
    items,
    totalAmount: Number(o.totalAmount || (items.length > 0 ? items.reduce((sum, i) => sum + i.subtotal, 0) : 0))
  };
}

function normalizeEmployee(e) {
  return {
    ...e,
    id: e.id,
    name: e.name || 'Employee',
    designation: e.designation || e.department || 'Warehouse Operator',
    hourlyRate: Number(e.hourlyRate !== undefined ? e.hourlyRate : (e.baseSalary || 200)),
    status: e.status || 'ACTIVE'
  };
}

export const SLMSProvider = ({ children }) => {
  const [user, setUser] = useState({
    isAuthenticated: true,
    username: 'Admin User',
    role: 'Warehouse Operations Manager'
  });

  const [products, setProducts] = useState(initialProducts || []);
  const [orders, setOrders] = useState(initialOrders || []);
  const [employees, setEmployees] = useState(initialEmployees || []);
  const [attendance, setAttendance] = useState(initialAttendance || []);
  const [reviews, setReviews] = useState(initialReviews || []);
  const [exchanges, setExchanges] = useState(initialExchanges || []);
  const [isLiveConnected, setIsLiveConnected] = useState(false);

  // Theme Management (Day Mode / Night Mode)
  const [theme, setTheme] = useState(() => {
    return localStorage.getItem('slms-theme') || 'light';
  });

  useEffect(() => {
    document.documentElement.setAttribute('data-theme', theme);
    if (theme === 'dark') {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
    localStorage.setItem('slms-theme', theme);
  }, [theme]);

  const toggleTheme = () => {
    setTheme((prev) => (prev === 'light' ? 'dark' : 'light'));
  };

  // Fetch initial data from Spring Boot backend if available
  const refreshAllData = async () => {
    try {
      const [
        fetchedProducts,
        fetchedEmployees,
        fetchedOrders,
        fetchedAttendance,
        fetchedReviews,
        fetchedReturns
      ] = await Promise.allSettled([
        apiService.getProducts(),
        apiService.getEmployees(),
        apiService.getOrders(),
        apiService.getAttendance(),
        apiService.getReviews(),
        apiService.getReturns()
      ]);

      let connected = false;

      if (fetchedProducts.status === 'fulfilled' && Array.isArray(fetchedProducts.value)) {
        setProducts(fetchedProducts.value.map(normalizeProduct));
        connected = true;
      }
      if (fetchedEmployees.status === 'fulfilled' && Array.isArray(fetchedEmployees.value)) {
        setEmployees(fetchedEmployees.value.map(normalizeEmployee));
        connected = true;
      }
      if (fetchedOrders.status === 'fulfilled' && Array.isArray(fetchedOrders.value)) {
        setOrders(fetchedOrders.value.map(normalizeOrder));
        connected = true;
      }
      if (fetchedAttendance.status === 'fulfilled' && Array.isArray(fetchedAttendance.value)) {
        setAttendance(fetchedAttendance.value);
        connected = true;
      }
      if (fetchedReviews.status === 'fulfilled' && Array.isArray(fetchedReviews.value)) {
        setReviews(fetchedReviews.value);
        connected = true;
      }
      if (fetchedReturns.status === 'fulfilled' && Array.isArray(fetchedReturns.value)) {
        setExchanges(fetchedReturns.value);
        connected = true;
      }

      setIsLiveConnected(connected);
    } catch (err) {
      console.warn('Backend server not connected or empty, defaulting to local state:', err.message);
    }
  };

  useEffect(() => {
    refreshAllData();
  }, []);

  // Authentication Mock (Restricted to admin / 12345)
  const login = (username, password) => {
    if (username === 'admin' && password === '12345') {
      setUser({
        isAuthenticated: true,
        username: 'admin',
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

  // --- Product CRUD Operations ---
  const addProduct = async (newProduct) => {
    const totQty = Number(newProduct.totalQuantity !== undefined ? newProduct.totalQuantity : newProduct.availableQuantity || 0);
    const availQty = Number(newProduct.availableQuantity !== undefined ? newProduct.availableQuantity : totQty);

    const productPayload = {
      id: newProduct.id,
      name: newProduct.name,
      category: newProduct.category || 'ELECTRONICS',
      type: newProduct.type || 'NonPerishableProduct',
      batchNumber: newProduct.batchNumber || `BAT-${Math.floor(100 + Math.random() * 900)}`,
      productionDate: newProduct.productionDate || new Date().toISOString().split('T')[0],
      expiryDate: newProduct.expiryDate || 'N/A',
      totalQuantity: totQty,
      availableQuantity: availQty,
      expiredQuantity: Number(newProduct.expiredQuantity || 0),
      costPrice: Number(newProduct.costPrice || 0),
      sellingPrice: Number(newProduct.sellingPrice || 0)
    };

    try {
      const created = await apiService.createProduct(productPayload);
      const normalized = normalizeProduct(created);
      const finalProduct = {
        ...normalized,
        totalQuantity: normalized.totalQuantity !== undefined ? normalized.totalQuantity : productPayload.totalQuantity,
        availableQuantity: normalized.availableQuantity !== undefined ? normalized.availableQuantity : productPayload.availableQuantity
      };
      setProducts((prev) => [finalProduct, ...prev]);
      return finalProduct;
    } catch (err) {
      console.warn('Backend create product error, storing locally:', err.message);
      const fallback = normalizeProduct({
        ...productPayload,
        id: productPayload.id || `PRD-${Math.floor(1000 + Math.random() * 9000)}`
      });
      setProducts((prev) => [fallback, ...prev]);
      return fallback;
    }
  };

  const updateProduct = async (updatedProduct) => {
    const id = updatedProduct.productId || updatedProduct.id;
    try {
      const result = await apiService.updateProduct(id, updatedProduct);
      const normalized = normalizeProduct(result);
      setProducts((prev) => prev.map((item) => (item.id === id || item.productId === id ? normalized : item)));
    } catch (err) {
      console.warn('Backend update product error, applying locally:', err.message);
      setProducts((prev) =>
        prev.map((item) => (item.id === id || item.productId === id ? normalizeProduct(updatedProduct) : item))
      );
    }
  };

  const deleteProduct = async (id) => {
    try {
      await apiService.deleteProduct(id);
    } catch (err) {
      console.warn('Backend delete product error, deleting locally:', err.message);
    }
    setProducts((prev) => prev.filter((item) => item.id !== id && item.productId !== id));
  };

  const addStock = async (id, quantity, batchNumber = null) => {
    try {
      const updated = await apiService.addStock(id, quantity, batchNumber);
      const normalized = normalizeProduct(updated);
      setProducts((prev) => prev.map((item) => (item.id === id || item.productId === id ? normalized : item)));
    } catch (err) {
      console.warn('Backend add stock error, updating locally:', err.message);
      setProducts((prev) =>
        prev.map((item) => {
          if (item.id === id || item.productId === id) {
            return {
              ...item,
              availableQuantity: Number(item.availableQuantity || 0) + Number(quantity)
            };
          }
          return item;
        })
      );
    }
  };

  const deductStock = async (id, quantity) => {
    const qty = Number(quantity || 1);

    // Optimistically update products state locally (only reduce availableQuantity, preserve totalQuantity)
    setProducts((prev) =>
      prev.map((item) => {
        if (item.id === id || item.productId === id) {
          const currentAvail = Number(item.availableQuantity !== undefined ? item.availableQuantity : item.totalQuantity || 0);
          return {
            ...item,
            availableQuantity: Math.max(0, currentAvail - qty)
          };
        }
        return item;
      })
    );

    try {
      await apiService.deductStock(id, qty);
    } catch (err) {
      console.warn('Backend deduct stock warning:', err.message);
    }
  };

  // --- Order CRUD Operations ---
  const createOrder = async (orderData) => {
    const rawItems = (orderData.items || []).map((item) => ({
      productId: item.productId || item.id,
      productName: item.productName || item.name || 'Product',
      quantity: Number(item.quantity || 1),
      unitPrice: Number(item.unitPrice || 0),
      subtotal: Number(item.subtotal || (Number(item.quantity || 1) * Number(item.unitPrice || 0)))
    }));

    const computedUnits = rawItems.reduce((sum, i) => sum + i.quantity, 0);

    const payload = {
      customerName: orderData.customerName || 'Walk-in Customer',
      orderDate: orderData.orderDate || new Date().toISOString().split('T')[0],
      status: orderData.status || 'PENDING',
      totalAmount: Number(orderData.totalAmount || (rawItems.length > 0 ? rawItems.reduce((sum, i) => sum + (i.subtotal || 0), 0) : 0)),
      itemCount: computedUnits > 0 ? computedUnits : rawItems.length,
      items: rawItems
    };

    try {
      const created = await apiService.createOrder(payload);
      const normalized = normalizeOrder(created);

      const finalOrder = {
        ...normalized,
        customerName: normalized.customerName && normalized.customerName !== 'Walk-in Customer' ? normalized.customerName : payload.customerName,
        itemCount: normalized.itemCount && Number(normalized.itemCount) > 0 ? Number(normalized.itemCount) : payload.itemCount,
        items: Array.isArray(normalized.items) && normalized.items.length > 0 ? normalized.items : payload.items,
        totalAmount: normalized.totalAmount || payload.totalAmount
      };

      setOrders((prev) => [finalOrder, ...prev]);
      return finalOrder;
    } catch (err) {
      console.warn('Backend create order error, storing locally:', err.message);
      const fallback = normalizeOrder({
        id: `ORD-${Math.floor(9000 + Math.random() * 1000)}`,
        ...payload
      });
      setOrders((prev) => [fallback, ...prev]);
      return fallback;
    }
  };

  const updateOrderStatus = async (id, newStatus) => {
    const targetOrder = orders.find((o) => o.id === id);

    try {
      const updated = await apiService.updateOrderStatus(id, newStatus);
      const normalized = normalizeOrder(updated);
      setOrders((prev) => prev.map((o) => (o.id === id ? normalized : o)));
    } catch (err) {
      console.warn('Backend update status error, updating locally:', err.message);
      setOrders((prev) =>
        prev.map((order) => (order.id === id ? { ...order, status: newStatus } : order))
      );
    }

    // Automatically deduct product stock when order is dispatched
    if (targetOrder && newStatus === 'DISPATCHED' && targetOrder.status !== 'DISPATCHED') {
      if (Array.isArray(targetOrder.items) && targetOrder.items.length > 0) {
        targetOrder.items.forEach((item) => {
          const itemProdId = item.productId || item.id;
          const qty = Number(item.quantity || 1);

          const matchedProd = products.find(
            (p) =>
              (p.id && String(p.id).toLowerCase() === String(itemProdId).toLowerCase()) ||
              (p.productId && String(p.productId).toLowerCase() === String(itemProdId).toLowerCase()) ||
              (p.name && item.productName && p.name.toLowerCase() === item.productName.toLowerCase())
          );

          if (matchedProd) {
            const prodId = matchedProd.id || matchedProd.productId;
            deductStock(prodId, qty);
          }
        });
      }
    }
  };

  const cancelOrder = async (id) => {
    try {
      const cancelled = await apiService.cancelOrder(id);
      const normalized = normalizeOrder(cancelled);
      setOrders((prev) => prev.map((o) => (o.id === id ? normalized : o)));
      // Refresh products state because backend automatically restored stock
      apiService.getProducts().then((prods) => setProducts(prods.map(normalizeProduct))).catch(() => {});
    } catch (err) {
      console.warn('Backend cancel order error, updating locally:', err.message);
      setOrders((prev) =>
        prev.map((order) => (order.id === id ? { ...order, status: 'CANCELLED' } : order))
      );
    }
  };

  // --- Employee CRUD Operations ---
  const addEmployee = async (empData) => {
    const payload = {
      id: empData.id,
      name: empData.name,
      designation: empData.designation || 'Warehouse Operator',
      hourlyRate: Number(empData.hourlyRate !== undefined ? empData.hourlyRate : 200),
      status: empData.status || 'ACTIVE'
    };

    try {
      const created = await apiService.createEmployee(payload);
      const normalized = normalizeEmployee(created);
      const finalEmp = {
        ...normalized,
        designation: normalized.designation && normalized.designation !== 'Staff' ? normalized.designation : payload.designation,
        hourlyRate: normalized.hourlyRate || payload.hourlyRate
      };
      setEmployees((prev) => [finalEmp, ...prev]);
      return finalEmp;
    } catch (err) {
      console.warn('Backend add employee error, storing locally:', err.message);
      const fallback = normalizeEmployee({
        ...payload,
        id: payload.id || `EMP-${Math.floor(200 + Math.random() * 800)}`
      });
      setEmployees((prev) => [fallback, ...prev]);
      return fallback;
    }
  };

  const updateEmployee = async (updatedEmp) => {
    const payload = {
      id: updatedEmp.id,
      name: updatedEmp.name,
      designation: updatedEmp.designation,
      hourlyRate: Number(updatedEmp.hourlyRate || 200),
      status: updatedEmp.status || 'ACTIVE'
    };

    try {
      const updated = await apiService.updateEmployee(updatedEmp.id, payload);
      const normalized = normalizeEmployee(updated);
      const finalEmp = {
        ...normalized,
        designation: normalized.designation || payload.designation,
        hourlyRate: normalized.hourlyRate || payload.hourlyRate
      };
      setEmployees((prev) => prev.map((emp) => (emp.id === updatedEmp.id ? finalEmp : emp)));
    } catch (err) {
      console.warn('Backend update employee error, updating locally:', err.message);
      setEmployees((prev) =>
        prev.map((emp) => (emp.id === updatedEmp.id ? normalizeEmployee(payload) : emp))
      );
    }
  };

  const deleteEmployee = async (id) => {
    try {
      await apiService.deleteEmployee(id);
    } catch (err) {
      console.warn('Backend delete employee error, removing locally:', err.message);
    }
    setEmployees((prev) => prev.filter((emp) => emp.id !== id));
  };

  // --- Attendance Operations ---
  const addAttendanceRecord = async (attData) => {
    const matchedEmp = employees.find(
      (e) => String(e.id || e.employeeId) === String(attData.employeeId)
    );

    const payload = {
      id: attData.id,
      employeeId: attData.employeeId,
      employeeName: attData.employeeName || (matchedEmp ? matchedEmp.name : 'Employee'),
      date: attData.date || new Date().toISOString().split('T')[0],
      status: attData.status || 'PRESENT',
      checkIn: attData.checkIn || '09:00',
      checkOut: attData.checkOut || '17:00',
      hoursWorked: Number(attData.hoursWorked || 8),
      notes: attData.notes || ''
    };

    try {
      const created = await apiService.recordAttendance(payload);
      const finalAtt = {
        ...created,
        employeeName: created.employeeName && created.employeeName !== 'Employee' ? created.employeeName : payload.employeeName,
        hoursWorked: created.hoursWorked !== undefined && created.hoursWorked > 0 ? created.hoursWorked : payload.hoursWorked,
        checkIn: created.checkIn || payload.checkIn,
        checkOut: created.checkOut || payload.checkOut,
        date: created.date || payload.date
      };
      setAttendance((prev) => [finalAtt, ...prev]);
      return finalAtt;
    } catch (err) {
      console.warn('Backend attendance error, storing locally:', err.message);
      const fallback = {
        ...payload,
        id: payload.id || `ATT-${Math.floor(500 + Math.random() * 500)}`
      };
      setAttendance((prev) => [fallback, ...prev]);
      return fallback;
    }
  };

  // --- Review Operations ---
  const addReview = async (revData) => {
    const payload = {
      id: revData.id,
      productId: revData.productId,
      productName: revData.productName || 'Product',
      customerName: revData.customerName || 'Customer',
      rating: Number(revData.rating || 5),
      reviewDate: revData.reviewDate || new Date().toISOString().split('T')[0],
      comment: revData.comment || ''
    };

    try {
      const created = await apiService.createReview(payload);
      const finalRev = {
        ...created,
        productName: created.productName && created.productName !== 'Product' ? created.productName : payload.productName,
        customerName: created.customerName && created.customerName !== 'Customer' ? created.customerName : payload.customerName,
        reviewDate: created.reviewDate || payload.reviewDate
      };
      setReviews((prev) => [finalRev, ...prev]);
      return finalRev;
    } catch (err) {
      console.warn('Backend review error, storing locally:', err.message);
      const fallback = {
        ...payload,
        id: payload.id || `REV-${Math.floor(300 + Math.random() * 700)}`
      };
      setReviews((prev) => [fallback, ...prev]);
      return fallback;
    }
  };

  // --- Exchange / Return Operations ---
  const addExchange = async (excData) => {
    const payload = {
      id: excData.id,
      orderId: excData.orderId || `ORD-${Math.floor(9000 + Math.random() * 1000)}`,
      productId: excData.productId,
      productName: excData.productName || 'Product',
      customerName: excData.customerName || 'Customer',
      reason: excData.reason || excData.returnReason || 'DAMAGED',
      exchangeDate: excData.exchangeDate || new Date().toISOString().split('T')[0],
      status: excData.status || 'Pending Inspection',
      notes: excData.notes || ''
    };

    try {
      const created = await apiService.createReturn(payload);
      const finalExc = {
        ...created,
        orderId: created.orderId || payload.orderId,
        productName: created.productName && created.productName !== 'Product' ? created.productName : payload.productName,
        customerName: created.customerName && created.customerName !== 'Customer' ? created.customerName : payload.customerName,
        reason: created.reason || payload.reason,
        exchangeDate: created.exchangeDate || payload.exchangeDate,
        status: created.status || payload.status,
        notes: created.notes || payload.notes
      };
      setExchanges((prev) => [finalExc, ...prev]);
      return finalExc;
    } catch (err) {
      console.warn('Backend return request error, storing locally:', err.message);
      const fallback = {
        ...payload,
        id: payload.id || `EXC-${Math.floor(400 + Math.random() * 600)}`
      };
      setExchanges((prev) => [fallback, ...prev]);
      return fallback;
    }
  };

  // Aggregated Dashboard Metrics
  const safeProducts = Array.isArray(products) ? products : [];
  const safeOrders = Array.isArray(orders) ? orders : [];
  const safeEmployees = Array.isArray(employees) ? employees : [];

  const totalProductsCount = safeProducts.length;
  const availableStockCount = safeProducts.reduce((acc, p) => acc + Number(p?.availableQuantity || 0), 0);
  const pendingOrdersCount = safeOrders.filter((o) => o?.status === 'PENDING').length;
  const monthlyRevenueTotal = safeOrders.reduce((acc, o) => acc + (o?.status !== 'CANCELLED' ? Number(o?.totalAmount || 0) : 0), 0);
  const activeEmployeesCount = safeEmployees.filter((e) => e?.active !== false && e?.status !== 'INACTIVE').length;
  const lowStockItems = safeProducts.filter((p) => Number(p?.availableQuantity || 0) < 100);

  return (
    <SLMSContext.Provider
      value={{
        user,
        login,
        logout,
        theme,
        toggleTheme,
        isLiveConnected,
        refreshAllData,
        products,
        addProduct,
        updateProduct,
        deleteProduct,
        addStock,
        deductStock,
        orders,
        createOrder,
        updateOrderStatus,
        cancelOrder,
        employees,
        addEmployee,
        updateEmployee,
        deleteEmployee,
        attendance,
        addAttendanceRecord,
        reviews,
        addReview,
        exchanges,
        mockMonthlyRevenue: mockMonthlyRevenue || [],
        mockPopularProducts: mockPopularProducts || [],
        mockSeasonalTrends: mockSeasonalTrends || [],
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

