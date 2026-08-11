import React from 'react';
import { Link } from 'react-router-dom';
import PageHeader from '../components/common/PageHeader';
import StatCard from '../components/common/StatCard';
import StatusBadge from '../components/common/StatusBadge';
import RevenueChart from '../components/reports/RevenueChart';
import {
  Package,
  Boxes,
  ShoppingCart,
  DollarSign,
  Users,
  AlertTriangle,
  ArrowRight,
  TrendingUp
} from 'lucide-react';
import { useSLMS } from '../context/SLMSContext';

// Category distribution doughnut chart
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { Doughnut } from 'react-chartjs-2';
ChartJS.register(ArcElement, Tooltip, Legend);

const Dashboard = () => {
  const {
    totalProductsCount,
    availableStockCount,
    pendingOrdersCount,
    monthlyRevenueTotal,
    activeEmployeesCount,
    lowStockItems,
    orders,
    products,
    mockMonthlyRevenue
  } = useSLMS();

  // Category counts for Doughnut chart
  const categoryCounts = {
    ELECTRONICS: 0,
    GROCERY: 0,
    PHARMACEUTICAL: 0,
    CLOTHING: 0,
    OTHER: 0
  };

  products.forEach((p) => {
    if (categoryCounts[p.category] !== undefined) {
      categoryCounts[p.category] += 1;
    } else {
      categoryCounts.OTHER += 1;
    }
  });

  const doughnutData = {
    labels: ['Electronics', 'Grocery', 'Pharmaceutical', 'Clothing', 'Other'],
    datasets: [
      {
        data: [
          categoryCounts.ELECTRONICS || 3,
          categoryCounts.GROCERY || 2,
          categoryCounts.PHARMACEUTICAL || 2,
          categoryCounts.CLOTHING || 1,
          categoryCounts.OTHER || 1
        ],
        backgroundColor: ['#2563eb', '#10b981', '#f59e0b', '#8b5cf6', '#64748b'],
        borderWidth: 2,
        borderColor: '#ffffff'
      }
    ]
  };

  const doughnutOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom',
        labels: { boxWidth: 10, font: { size: 11 } }
      }
    }
  };

  const recentOrders = orders.slice(0, 5);

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <PageHeader
        title="Dashboard Overview"
        subtitle="Overview of warehouse operations and business performance."
      >
        <Link to="/orders" className="btn btn-sm bg-blue-600 hover:bg-blue-700 text-white rounded-xl gap-1.5 border-0">
          <ShoppingCart className="w-4 h-4" />
          <span>New Order</span>
        </Link>
      </PageHeader>

      {/* KPI Cards Grid (6 cards) */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        <StatCard
          title="Total Products"
          value={totalProductsCount || 248}
          subtitle="Products in catalog"
          icon={Package}
          badgeText="Active Catalog"
          badgeColor="badge-neutral"
        />
        <StatCard
          title="Available Stock"
          value={(availableStockCount || 12480).toLocaleString()}
          subtitle="Units currently available"
          icon={Boxes}
          badgeText="Warehouse Stock"
          badgeColor="badge-success"
        />
        <StatCard
          title="Pending Orders"
          value={pendingOrdersCount || 18}
          subtitle="Orders awaiting confirmation"
          icon={ShoppingCart}
          badgeText="Action Required"
          badgeColor="badge-warning"
        />
        <StatCard
          title="Monthly Revenue"
          value={`৳${(monthlyRevenueTotal || 485200).toLocaleString()}`}
          subtitle="Current month total"
          icon={DollarSign}
          badgeText="+14% YoY"
          badgeColor="badge-success"
        />
        <StatCard
          title="Active Employees"
          value={activeEmployeesCount || 32}
          subtitle="Currently active workforce"
          icon={Users}
          badgeText="On Duty"
          badgeColor="badge-info"
        />
        <StatCard
          title="Low Stock Items"
          value={lowStockItems.length || 7}
          subtitle="Products need reorder attention"
          icon={AlertTriangle}
          badgeText="Attention Required"
          badgeColor="badge-error"
        />
      </div>

      {/* Charts Section */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Monthly Revenue Bar Chart (2 cols) */}
        <div className="lg:col-span-2 bg-white rounded-2xl border border-slate-200 p-5 shadow-2xs space-y-4">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="font-bold text-slate-900 text-base">Monthly Revenue Trend</h3>
              <p className="text-xs text-slate-500">Sales fulfillment overview across past months</p>
            </div>
            <span className="text-xs font-semibold text-emerald-600 flex items-center gap-1 bg-emerald-50 px-2.5 py-1 rounded-lg">
              <TrendingUp className="w-3.5 h-3.5" /> High Performance
            </span>
          </div>
          <RevenueChart data={mockMonthlyRevenue} />
        </div>

        {/* Product Category Distribution Doughnut Chart (1 col) */}
        <div className="bg-white rounded-2xl border border-slate-200 p-5 shadow-2xs space-y-4">
          <div>
            <h3 className="font-bold text-slate-900 text-base">Category Breakdown</h3>
            <p className="text-xs text-slate-500">Inventory catalog category ratio</p>
          </div>
          <div className="h-60 w-full">
            <Doughnut data={doughnutData} options={doughnutOptions} />
          </div>
        </div>
      </div>

      {/* Tables & Attention Alerts Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Recent Orders Compact Table (2 cols) */}
        <div className="lg:col-span-2 bg-white rounded-2xl border border-slate-200 p-5 shadow-2xs space-y-4">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="font-bold text-slate-900 text-base">Recent Customer Orders</h3>
              <p className="text-xs text-slate-500">Latest 5 sales transactions</p>
            </div>
            <Link to="/orders" className="text-xs font-bold text-blue-600 hover:underline flex items-center gap-1">
              View All Orders <ArrowRight className="w-3.5 h-3.5" />
            </Link>
          </div>

          <div className="overflow-x-auto">
            <table className="table table-sm w-full">
              <thead>
                <tr className="bg-slate-50 text-slate-500 text-xs">
                  <th className="py-2.5 px-3">Order ID</th>
                  <th className="py-2.5 px-3">Customer</th>
                  <th className="py-2.5 px-3">Date</th>
                  <th className="py-2.5 px-3">Amount</th>
                  <th className="py-2.5 px-3">Status</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 text-xs font-medium">
                {recentOrders.map((o) => (
                  <tr key={o.id} className="hover:bg-slate-50">
                    <td className="py-2.5 px-3 font-mono font-bold text-blue-600">{o.id}</td>
                    <td className="py-2.5 px-3 font-semibold text-slate-900">{o.customerName}</td>
                    <td className="py-2.5 px-3 text-slate-500">{o.orderDate}</td>
                    <td className="py-2.5 px-3 font-bold text-slate-800">৳{o.totalAmount.toLocaleString()}</td>
                    <td className="py-2.5 px-3">
                      <StatusBadge status={o.status} />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        {/* Low Stock / Near Expiry Attention List (1 col) */}
        <div className="bg-white rounded-2xl border border-slate-200 p-5 shadow-2xs space-y-4">
          <div className="flex items-center justify-between">
            <h3 className="font-bold text-slate-900 text-base">Low Stock & Expiry Alerts</h3>
            <span className="badge badge-warning text-[10px] font-bold">Action Needed</span>
          </div>

          <div className="space-y-2.5">
            {lowStockItems.length === 0 ? (
              <p className="text-xs text-slate-500">All inventory items are currently well-stocked.</p>
            ) : (
              lowStockItems.map((p) => (
                <div
                  key={p.id}
                  className="flex items-center justify-between p-3 bg-amber-50/60 border border-amber-200/80 rounded-xl text-xs"
                >
                  <div>
                    <span className="font-bold text-slate-900 block">{p.name}</span>
                    <span className="text-[11px] text-amber-800 font-medium">
                      Batch: {p.batchNumber} • Category: {p.category}
                    </span>
                  </div>
                  <div className="text-right">
                    <span className="font-extrabold text-amber-900 bg-amber-200/80 px-2 py-0.5 rounded text-[11px]">
                      {p.availableQuantity} units left
                    </span>
                  </div>
                </div>
              ))
            )}

            <div className="flex items-center justify-between p-3 bg-rose-50/60 border border-rose-200/80 rounded-xl text-xs">
              <div>
                <span className="font-bold text-slate-900 block">Whole Milk Powder (1kg)</span>
                <span className="text-[11px] text-rose-800 font-medium">Near expiry batch: 2026-08-20</span>
              </div>
              <span className="font-bold text-rose-700 text-[10px] bg-rose-100 px-2 py-0.5 rounded">
                Near Expiry
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
