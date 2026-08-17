import React, { useState, useMemo } from 'react';
import PageHeader from '../components/common/PageHeader';
import RevenueChart from '../components/reports/RevenueChart';
import ProductPopularityChart from '../components/reports/ProductPopularityChart';
import RatingChart from '../components/reports/RatingChart';
import ExchangeChart from '../components/reports/ExchangeChart';
import SeasonalTrendChart from '../components/reports/SeasonalTrendChart';
import StatCard from '../components/common/StatCard';
import { DollarSign, Award, TrendingUp, Calendar } from 'lucide-react';
import { useSLMS } from '../context/SLMSContext';

const Reports = () => {
  const { orders = [], products = [], reviews = [], exchanges = [] } = useSLMS();
  const [selectedYear, setSelectedYear] = useState('2026');

  // 1. Dynamic Real-Time Monthly Revenue
  const monthlyRevenueData = useMemo(() => {
    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    const map = {};
    months.forEach((m) => { map[m] = 0; });

    // Sum revenue from non-cancelled orders
    orders.forEach((order) => {
      if (order.status === 'CANCELLED') return;
      let dateObj = new Date();
      if (order.orderDate) {
        dateObj = new Date(order.orderDate);
      }
      if (!isNaN(dateObj.getTime())) {
        const monthName = months[dateObj.getMonth()];
        const amount = Number(order.totalAmount || order.totalPrice || 0);
        map[monthName] += amount;
      }
    });

    return months.map((m) => ({
      month: m,
      revenue: map[m]
    }));
  }, [orders]);

  // 2. Dynamic Real-Time Popular Products (Top Sellers)
  const popularProductsData = useMemo(() => {
    const salesMap = {};

    // Step A: Initialize base sold count from product catalog stock diff (totalQuantity - availableQuantity)
    products.forEach((p) => {
      const name = p.name || 'Unnamed Product';
      const stockDiff = Math.max(0, Number(p.totalQuantity || 0) - Number(p.availableQuantity || 0));
      salesMap[name] = stockDiff;
    });

    // Step B: Aggregate units sold from all non-cancelled customer orders
    orders.forEach((order) => {
      if (order.status === 'CANCELLED') return;
      if (Array.isArray(order.items) && order.items.length > 0) {
        order.items.forEach((item) => {
          const rawName = item.productName || item.name || '';
          if (!rawName) return;

          const qty = Number(item.quantity || 1);

          // Match product name in salesMap (case-insensitive)
          const matchedKey = Object.keys(salesMap).find(
            (k) => k.toLowerCase() === rawName.toLowerCase()
          );

          if (matchedKey) {
            salesMap[matchedKey] += qty;
          } else {
            salesMap[rawName] = (salesMap[rawName] || 0) + qty;
          }
        });
      }
    });

    const result = Object.keys(salesMap).map((name) => ({
      name,
      sold: salesMap[name]
    }));

    result.sort((a, b) => b.sold - a.sold);
    return result.slice(0, 6);
  }, [orders, products]);

  // 3. Dynamic Real-Time Seasonal Sales Trends
  const seasonalTrendData = useMemo(() => {
    const seasonCounts = { Q1: 0, Q2: 0, Q3: 0, Q4: 0 };

    orders.forEach((order) => {
      if (order.status === 'CANCELLED') return;
      let dateObj = new Date();
      if (order.orderDate) {
        dateObj = new Date(order.orderDate);
      }

      if (!isNaN(dateObj.getTime())) {
        const month = dateObj.getMonth();
        const itemCount = Array.isArray(order.items) && order.items.length > 0
          ? order.items.reduce((acc, item) => acc + Number(item.quantity || 1), 0)
          : 1;

        if (month >= 0 && month <= 2) seasonCounts.Q1 += itemCount;
        else if (month >= 3 && month <= 5) seasonCounts.Q2 += itemCount;
        else if (month >= 6 && month <= 8) seasonCounts.Q3 += itemCount;
        else seasonCounts.Q4 += itemCount;
      }
    });

    return [
      { season: 'Q1 (Jan-Mar)', sales: seasonCounts.Q1 },
      { season: 'Q2 (Apr-Jun)', sales: seasonCounts.Q2 },
      { season: 'Q3 (Jul-Sep)', sales: seasonCounts.Q3 },
      { season: 'Q4 (Oct-Dec)', sales: seasonCounts.Q4 }
    ];
  }, [orders]);

  // Revenue Summary Metrics
  const totalRev = monthlyRevenueData.reduce((sum, item) => sum + item.revenue, 0);
  const activeMonthsCount = monthlyRevenueData.filter((item) => item.revenue > 0).length || 1;
  const avgRev = Math.round(totalRev / activeMonthsCount);
  const maxRevMonth = monthlyRevenueData.reduce((max, item) => (item.revenue > (max?.revenue || 0) ? item : max), monthlyRevenueData[0] || null);

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <PageHeader
        title="Reports & Analytics"
        subtitle="Real-time sales, inventory, customer feedback, and return performance."
      >
        <div className="flex items-center gap-2">
          <Calendar className="w-4 h-4 text-slate-500" />
          <select
            value={selectedYear}
            onChange={(e) => setSelectedYear(e.target.value)}
            className="select select-sm border-slate-200 text-xs rounded-xl font-bold text-slate-700"
          >
            <option value="2026">Fiscal Year 2026</option>
            <option value="2025">Fiscal Year 2025</option>
          </select>
        </div>
      </PageHeader>

      {/* KPI Cards for Revenue Summary */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <StatCard
          title="Total Fiscal Revenue"
          value={`৳${totalRev.toLocaleString()}`}
          subtitle="Real-time cumulative YTD revenue"
          icon={DollarSign}
          badgeText="Live Synced"
          badgeColor="badge-success"
        />
        <StatCard
          title="Average Monthly Revenue"
          value={`৳${avgRev.toLocaleString()}`}
          subtitle="Monthly run-rate benchmark"
          icon={TrendingUp}
          badgeText="Active Run-Rate"
          badgeColor="badge-info"
        />
        <StatCard
          title="Peak Revenue Month"
          value={maxRevMonth && maxRevMonth.revenue > 0 ? maxRevMonth.month : 'August'}
          subtitle={`Highest revenue: ৳${maxRevMonth ? maxRevMonth.revenue.toLocaleString() : '0'}`}
          icon={Award}
          badgeText="Top Sales"
          badgeColor="badge-warning"
        />
      </div>

      {/* Grid 1: Revenue & Popular Products */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Monthly Revenue Chart */}
        <div className="bg-white rounded-2xl border border-slate-200 p-5 shadow-2xs space-y-3">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="font-bold text-slate-900 text-base">Monthly Revenue Report</h3>
              <p className="text-xs text-slate-500">Live order revenue by month</p>
            </div>
            <span className="text-[10px] font-bold px-2 py-0.5 rounded-full bg-emerald-100 text-emerald-700">
              Live Updates
            </span>
          </div>
          <RevenueChart data={monthlyRevenueData} />
        </div>

        {/* Popular Products Chart */}
        <div className="bg-white rounded-2xl border border-slate-200 p-5 shadow-2xs space-y-3">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="font-bold text-slate-900 text-base">Popular Products (Top Sellers)</h3>
              <p className="text-xs text-slate-500">Product sales quantity leaderboard ranking</p>
            </div>
            <span className="text-[10px] font-bold px-2 py-0.5 rounded-full bg-blue-100 text-blue-700">
              Real-Time Stock
            </span>
          </div>
          <ProductPopularityChart data={popularProductsData} />
        </div>
      </div>

      {/* Grid 2: Ratings, Exchanges & Seasonal Trends */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {/* Rating Breakdown */}
        <div className="bg-white rounded-2xl border border-slate-200 p-5 shadow-2xs space-y-3">
          <div>
            <h3 className="font-bold text-slate-900 text-base">Product Rating Distribution</h3>
            <p className="text-xs text-slate-500">Customer feedback score breakdown</p>
          </div>
          <RatingChart reviews={reviews} />
        </div>

        {/* Return / Exchange Reasons */}
        <div className="bg-white rounded-2xl border border-slate-200 p-5 shadow-2xs space-y-3">
          <div>
            <h3 className="font-bold text-slate-900 text-base">Exchange &amp; Return Statistics</h3>
            <p className="text-xs text-slate-500">Breakdown of return logistics reasons</p>
          </div>
          <ExchangeChart exchanges={exchanges} />
        </div>

        {/* Seasonal Trends */}
        <div className="bg-white rounded-2xl border border-slate-200 p-5 shadow-2xs space-y-3">
          <div>
            <h3 className="font-bold text-slate-900 text-base">Seasonal Sales Trends</h3>
            <p className="text-xs text-slate-500">Quarterly unit volume distribution</p>
          </div>
          <SeasonalTrendChart data={seasonalTrendData} />
        </div>
      </div>
    </div>
  );
};

export default Reports;
