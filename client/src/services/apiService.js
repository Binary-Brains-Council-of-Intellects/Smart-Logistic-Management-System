const API_BASE_URL = 'https://slms-backend-1u96.onrender.com/api';

/**
 * Helper to perform HTTP fetch requests to the Spring Boot REST backend
 */
async function fetchJson(endpoint, options = {}) {
  const url = endpoint.startsWith('http') ? endpoint : `${API_BASE_URL}${endpoint}`;
  const response = await fetch(url, {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  });

  if (!response.ok) {
    const errorObj = await response.json().catch(() => ({}));
    throw new Error(errorObj.message || `Server request failed with status ${response.status}`);
  }

  const result = await response.json();
  // Return nested data field if present (ApiResponse format), otherwise return full result
  return result.data !== undefined ? result.data : result;
}

export const apiService = {
  // --- Products ---
  async getProducts() {
    return fetchJson('/products');
  },
  async getProductById(id) {
    return fetchJson(`/products/${id}`);
  },
  async createProduct(productData) {
    return fetchJson('/products', {
      method: 'POST',
      body: JSON.stringify(productData),
    });
  },
  async updateProduct(id, productData) {
    return fetchJson(`/products/${id}`, {
      method: 'PUT',
      body: JSON.stringify(productData),
    });
  },
  async addStock(id, quantity, batchNumber = null) {
    return fetchJson(`/products/${id}/add-stock`, {
      method: 'PATCH',
      body: JSON.stringify({ quantity, batchNumber }),
    });
  },
  async deductStock(id, quantity) {
    return fetchJson(`/products/${id}/deduct-stock`, {
      method: 'PATCH',
      body: JSON.stringify({ quantity }),
    });
  },
  async deleteProduct(id) {
    return fetchJson(`/products/${id}`, {
      method: 'DELETE',
    });
  },

  // --- Employees ---
  async getEmployees() {
    return fetchJson('/employees');
  },
  async getEmployeeById(id) {
    return fetchJson(`/employees/${id}`);
  },
  async createEmployee(employeeData) {
    return fetchJson('/employees', {
      method: 'POST',
      body: JSON.stringify(employeeData),
    });
  },
  async updateEmployee(id, employeeData) {
    return fetchJson(`/employees/${id}`, {
      method: 'PUT',
      body: JSON.stringify(employeeData),
    });
  },
  async deleteEmployee(id) {
    return fetchJson(`/employees/${id}`, {
      method: 'DELETE',
    });
  },
  async deactivateEmployee(id) {
    return fetchJson(`/employees/${id}/deactivate`, {
      method: 'PATCH',
    });
  },

  // --- Attendance ---
  async getAttendance() {
    return fetchJson('/attendance');
  },
  async getAttendanceByEmployee(employeeId) {
    return fetchJson(`/attendance/employee/${employeeId}`);
  },
  async recordAttendance(attendanceData) {
    return fetchJson('/attendance', {
      method: 'POST',
      body: JSON.stringify(attendanceData),
    });
  },
  async deleteAttendance(id) {
    return fetchJson(`/attendance/${id}`, {
      method: 'DELETE',
    });
  },

  // --- Orders ---
  async getOrders() {
    return fetchJson('/orders');
  },
  async getOrderById(id) {
    return fetchJson(`/orders/${id}`);
  },
  async createOrder(orderData) {
    return fetchJson('/orders', {
      method: 'POST',
      body: JSON.stringify(orderData),
    });
  },
  async updateOrderStatus(id, status) {
    return fetchJson(`/orders/${id}/status?status=${status}`, {
      method: 'PATCH',
    });
  },
  async cancelOrder(id) {
    return fetchJson(`/orders/${id}/cancel`, {
      method: 'PATCH',
    });
  },

  // --- Customers ---
  async getCustomers() {
    return fetchJson('/customers');
  },
  async createCustomer(customerData) {
    return fetchJson('/customers', {
      method: 'POST',
      body: JSON.stringify(customerData),
    });
  },
  async updateCustomer(id, customerData) {
    return fetchJson(`/customers/${id}`, {
      method: 'PUT',
      body: JSON.stringify(customerData),
    });
  },
  async deleteCustomer(id) {
    return fetchJson(`/customers/${id}`, {
      method: 'DELETE',
    });
  },

  // --- Returns & Exchanges ---
  async getReturns() {
    return fetchJson('/returns');
  },
  async createReturn(returnData) {
    return fetchJson('/returns', {
      method: 'POST',
      body: JSON.stringify(returnData),
    });
  },
  async approveReturn(id) {
    return fetchJson(`/returns/${id}/approve`, {
      method: 'PATCH',
    });
  },
  async rejectReturn(id) {
    return fetchJson(`/returns/${id}/reject`, {
      method: 'PATCH',
    });
  },
  async completeReturn(id) {
    return fetchJson(`/returns/${id}/complete`, {
      method: 'PATCH',
    });
  },

  // --- Reviews ---
  async getReviews() {
    return fetchJson('/reviews');
  },
  async createReview(reviewData) {
    return fetchJson('/reviews', {
      method: 'POST',
      body: JSON.stringify(reviewData),
    });
  },
  async deleteReview(id) {
    return fetchJson(`/reviews/${id}`, {
      method: 'DELETE',
    });
  },

  // --- Payroll ---
  async getAllPayroll() {
    return fetchJson('/payroll');
  },
  async generatePayroll(payrollData) {
    return fetchJson('/payroll', {
      method: 'POST',
      body: JSON.stringify(payrollData),
    });
  },
  async generateAllPayroll(month, year) {
    return fetchJson(`/payroll/generate-all?month=${month}&year=${year}`, {
      method: 'POST',
    });
  },

  // --- Reports & Analytics ---
  async getDashboardReport() {
    return fetchJson('/reports/dashboard');
  },
  async getSalesReport() {
    return fetchJson('/reports/sales');
  },
  async getInventoryReport() {
    return fetchJson('/reports/inventory');
  },
  async getWorkforceReport() {
    return fetchJson('/reports/workforce');
  },
};

