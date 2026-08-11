// Initial mock dataset for Smart Logistics Management System (SLMS)

export const initialProducts = [
  {
    id: "PRD-1001",
    name: "Wireless Ergonomic Mouse",
    category: "ELECTRONICS",
    type: "NonPerishableProduct",
    batchNumber: "B2025-01",
    productionDate: "2025-01-10",
    expiryDate: "",
    totalQuantity: 300,
    availableQuantity: 8,
    expiredQuantity: 0,
    costPrice: 1200,
    sellingPrice: 1650,
  },
  {
    id: "PRD-1002",
    name: "Whole Milk Powder (1kg)",
    category: "GROCERY",
    type: "PerishableProduct",
    batchNumber: "B2025-88",
    productionDate: "2025-05-01",
    expiryDate: "2026-08-20",
    totalQuantity: 500,
    availableQuantity: 140,
    expiredQuantity: 10,
    costPrice: 650,
    sellingPrice: 820,
  },
  {
    id: "PRD-1003",
    name: "Paracetamol 500mg (Box of 100)",
    category: "PHARMACEUTICAL",
    type: "PerishableProduct",
    batchNumber: "B2024-99",
    productionDate: "2024-02-15",
    expiryDate: "2026-02-15",
    totalQuantity: 400,
    availableQuantity: 0,
    expiredQuantity: 50,
    costPrice: 180,
    sellingPrice: 250,
  },
  {
    id: "PRD-1004",
    name: "High-Speed USB-C Cable 2m",
    category: "ELECTRONICS",
    type: "NonPerishableProduct",
    batchNumber: "B2025-04",
    productionDate: "2025-03-01",
    expiryDate: "",
    totalQuantity: 600,
    availableQuantity: 5,
    expiredQuantity: 0,
    costPrice: 250,
    sellingPrice: 450,
  },
  {
    id: "PRD-1005",
    name: "Men's Cotton Polo Shirt (L)",
    category: "CLOTHING",
    type: "NonPerishableProduct",
    batchNumber: "B2025-12",
    productionDate: "2025-04-10",
    expiryDate: "",
    totalQuantity: 250,
    availableQuantity: 110,
    expiredQuantity: 0,
    costPrice: 550,
    sellingPrice: 890,
  },
  {
    id: "PRD-1006",
    name: "Organic Instant Coffee 200g",
    category: "GROCERY",
    type: "PerishableProduct",
    batchNumber: "B2025-33",
    productionDate: "2025-06-01",
    expiryDate: "2027-06-01",
    totalQuantity: 350,
    availableQuantity: 220,
    expiredQuantity: 0,
    costPrice: 480,
    sellingPrice: 690,
  },
  {
    id: "PRD-1007",
    name: "Mechanical Gaming Keyboard",
    category: "ELECTRONICS",
    type: "NonPerishableProduct",
    batchNumber: "B2025-19",
    productionDate: "2025-02-20",
    expiryDate: "",
    totalQuantity: 150,
    availableQuantity: 45,
    expiredQuantity: 0,
    costPrice: 2800,
    sellingPrice: 3800,
  },
  {
    id: "PRD-1008",
    name: "Vitamin C Chewable Tablets",
    category: "PHARMACEUTICAL",
    type: "PerishableProduct",
    batchNumber: "B2025-50",
    productionDate: "2025-01-15",
    expiryDate: "2026-09-10",
    totalQuantity: 600,
    availableQuantity: 180,
    expiredQuantity: 0,
    costPrice: 320,
    sellingPrice: 480,
  },
  {
    id: "PRD-1009",
    name: "Safety Work Gloves (Pair)",
    category: "OTHER",
    type: "NonPerishableProduct",
    batchNumber: "B2025-02",
    productionDate: "2025-01-05",
    expiryDate: "",
    totalQuantity: 1000,
    availableQuantity: 640,
    expiredQuantity: 0,
    costPrice: 120,
    sellingPrice: 200,
  }
];

export const initialOrders = [
  {
    id: "ORD-9001",
    customerName: "Apex Retailers Ltd.",
    orderDate: "2026-08-10",
    status: "PENDING",
    items: [
      { productId: "PRD-1001", productName: "Wireless Ergonomic Mouse", quantity: 5, unitPrice: 1650, subtotal: 8250 },
      { productId: "PRD-1004", productName: "High-Speed USB-C Cable 2m", quantity: 10, unitPrice: 450, subtotal: 4500 }
    ],
    totalAmount: 12750
  },
  {
    id: "ORD-9002",
    customerName: "Green Life Supermarket",
    orderDate: "2026-08-09",
    status: "CONFIRMED",
    items: [
      { productId: "PRD-1002", productName: "Whole Milk Powder (1kg)", quantity: 40, unitPrice: 820, subtotal: 32800 },
      { productId: "PRD-1006", productName: "Organic Instant Coffee 200g", quantity: 20, unitPrice: 690, subtotal: 13800 }
    ],
    totalAmount: 46600
  },
  {
    id: "ORD-9003",
    customerName: "PharmaCare Distributors",
    orderDate: "2026-08-08",
    status: "DISPATCHED",
    items: [
      { productId: "PRD-1008", productName: "Vitamin C Chewable Tablets", quantity: 100, unitPrice: 480, subtotal: 48000 }
    ],
    totalAmount: 48000
  },
  {
    id: "ORD-9004",
    customerName: "Urban Style Boutique",
    orderDate: "2026-08-05",
    status: "CANCELLED",
    items: [
      { productId: "PRD-1005", productName: "Men's Cotton Polo Shirt (L)", quantity: 30, unitPrice: 890, subtotal: 26700 }
    ],
    totalAmount: 26700
  },
  {
    id: "ORD-9005",
    customerName: "TechZone Bangladesh",
    orderDate: "2026-08-03",
    status: "DISPATCHED",
    items: [
      { productId: "PRD-1007", productName: "Mechanical Gaming Keyboard", quantity: 15, unitPrice: 3800, subtotal: 57000 },
      { productId: "PRD-1001", productName: "Wireless Ergonomic Mouse", quantity: 15, unitPrice: 1650, subtotal: 24750 }
    ],
    totalAmount: 81750
  }
];

export const initialEmployees = [
  {
    id: "EMP-201",
    name: "Rahim Ahmed",
    designation: "Warehouse Supervisor",
    hourlyRate: 250,
    status: "ACTIVE"
  },
  {
    id: "EMP-202",
    name: "Fatema Tuz Zohra",
    designation: "Inventory Specialist",
    hourlyRate: 200,
    status: "ACTIVE"
  },
  {
    id: "EMP-203",
    name: "Tanvir Hossain",
    designation: "Forklift Operator",
    hourlyRate: 180,
    status: "ACTIVE"
  },
  {
    id: "EMP-204",
    name: "Shakib Al Hasan",
    designation: "Logistics Manager",
    hourlyRate: 350,
    status: "ACTIVE"
  },
  {
    id: "EMP-205",
    name: "Nusrat Jahan",
    designation: "Quality Control Officer",
    hourlyRate: 220,
    status: "INACTIVE"
  }
];

export const initialAttendance = [
  {
    id: "ATT-501",
    employeeId: "EMP-201",
    employeeName: "Rahim Ahmed",
    date: "2026-08-10",
    checkIn: "08:30 AM",
    checkOut: "05:30 PM",
    hoursWorked: 9
  },
  {
    id: "ATT-502",
    employeeId: "EMP-202",
    employeeName: "Fatema Tuz Zohra",
    date: "2026-08-10",
    checkIn: "09:00 AM",
    checkOut: "05:00 PM",
    hoursWorked: 8
  },
  {
    id: "ATT-503",
    employeeId: "EMP-203",
    employeeName: "Tanvir Hossain",
    date: "2026-08-10",
    checkIn: "08:00 AM",
    checkOut: "04:30 PM",
    hoursWorked: 8.5
  },
  {
    id: "ATT-504",
    employeeId: "EMP-204",
    employeeName: "Shakib Al Hasan",
    date: "2026-08-10",
    checkIn: "09:00 AM",
    checkOut: "06:00 PM",
    hoursWorked: 9
  }
];

export const initialReviews = [
  {
    id: "REV-301",
    productId: "PRD-1007",
    productName: "Mechanical Gaming Keyboard",
    customerName: "Tariqul Islam",
    rating: 5,
    reviewDate: "2026-08-01",
    comment: "Outstanding mechanical switches and durable metal chassis. Fast dispatch!"
  },
  {
    id: "REV-302",
    productId: "PRD-1002",
    productName: "Whole Milk Powder (1kg)",
    customerName: "Green Life Supermarket",
    rating: 4,
    reviewDate: "2026-07-28",
    comment: "Fresh packaging and long validity date. High customer demand."
  },
  {
    id: "REV-303",
    productId: "PRD-1001",
    productName: "Wireless Ergonomic Mouse",
    customerName: "Mahmudul Hasan",
    rating: 3,
    reviewDate: "2026-07-22",
    comment: "Good comfort, but battery compartment cover is slightly tight."
  },
  {
    id: "REV-304",
    productId: "PRD-1004",
    productName: "High-Speed USB-C Cable 2m",
    customerName: "Apex Retailers Ltd.",
    rating: 5,
    reviewDate: "2026-07-15",
    comment: "Excellent braided quality and fast charging throughput."
  }
];

export const initialExchanges = [
  {
    id: "EXC-401",
    orderId: "ORD-9004",
    productId: "PRD-1005",
    productName: "Men's Cotton Polo Shirt (L)",
    customerName: "Urban Style Boutique",
    reason: "WRONG_PRODUCT",
    exchangeDate: "2026-08-06",
    status: "Resolved",
    notes: "Shipped XL instead of L size. Correct replacement sent."
  },
  {
    id: "EXC-402",
    orderId: "ORD-8890",
    productId: "PRD-1001",
    productName: "Wireless Ergonomic Mouse",
    customerName: "Smart Office Supplies",
    reason: "DAMAGED",
    exchangeDate: "2026-07-25",
    status: "Pending Inspection",
    notes: "Cracked casing upon box opening."
  },
  {
    id: "EXC-403",
    orderId: "ORD-8850",
    productId: "PRD-1003",
    productName: "Paracetamol 500mg (Box of 100)",
    customerName: "City Medicos",
    reason: "EXPIRED_ON_ARRIVAL",
    exchangeDate: "2026-07-10",
    status: "Resolved",
    notes: "Batch expired within 10 days of delivery. Refund issued."
  }
];

export const mockMonthlyRevenue = [
  { month: "January", revenue: 320000 },
  { month: "February", revenue: 350000 },
  { month: "March", revenue: 410000 },
  { month: "April", revenue: 385000 },
  { month: "May", revenue: 450000 },
  { month: "June", revenue: 485200 },
  { month: "July", revenue: 510000 },
  { month: "August", revenue: 485200 }
];

export const mockPopularProducts = [
  { name: "Wireless Ergonomic Mouse", sold: 520 },
  { name: "High-Speed USB-C Cable 2m", sold: 470 },
  { name: "Mechanical Gaming Keyboard", sold: 420 },
  { name: "Whole Milk Powder (1kg)", sold: 380 },
  { name: "Organic Instant Coffee 200g", sold: 310 }
];

export const mockSeasonalTrends = [
  { season: "Spring", sales: 1250 },
  { season: "Summer", sales: 1840 },
  { season: "Autumn", sales: 1420 },
  { season: "Winter", sales: 980 }
];
