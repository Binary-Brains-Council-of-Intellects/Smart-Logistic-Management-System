import React, { useState } from 'react';
import PageHeader from '../components/common/PageHeader';
import RevenueChart from '../components/reports/RevenueChart';
import ProductPopularityChart from '../components/reports/ProductPopularityChart';
import RatingChart from '../components/reports/RatingChart';
import ExchangeChart from '../components/reports/ExchangeChart';
import SeasonalTrendChart from '../components/reports/SeasonalTrendChart';
import StatCard from '../components/common/StatCard';
import { DollarSign, Award, TrendingUp, RotateCcw, Calendar } from 'lucide-react';
import { useSLMS } from '../context/SLMSContext';

const Reports = () => {
  const { mockMonthlyRevenue = [], mockPopularProducts = [], mockSeasonalTrends = [], exchanges = [] } = useSLMS();
  const [selectedYear, setSelectedYear] = useState('2026');

  const safeMonthlyRev = Array.isArray(mockMonthlyRevenue) ? mockMonthlyRevenue : [];
  const safePopularProds = Array.isArray(mockPopularProducts) ? mockPopularProducts : [];
  const safeTrends = Array.isArray(mockSeasonalTrends) ? mockSeasonalTrends : [];

  // Revenue summary metrics
  const totalRev = safeMonthlyRev.reduce((sum, item) => sum + (item?.revenue || 0), 0);
  const avgRev = safeMonthlyRev.length > 0 ? Math.round(totalRev / safeMonthlyRev.length) : 0;
  const maxRevMonth = safeMonthlyRev.reduce((max, item) => ((item?.revenue || 0) > (max?.revenue || 0) ? item : max), safeMonthlyRev[0] || null);

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <PageHeader
        title="Reports & Analytics"
        subtitle="View sales, inventory, customer feedback, and return insights."
      >
        <div className="flex items-center gap-2">
          <Calendar className="w-4 h-4 text-slate-500" />
          <select
            value={selectedYear}
            onChange={(e) => setSelectedYear(e.target.value)}
            className="select select-sm border-slate-200 text-xs rounded-xl font-bold"
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
          subtitle="Cumulative YTD revenue"
          icon={DollarSign}
          badgeText="Target Achieved"
          badgeColor="badge-success"
        />
        <StatCard
          title="Average Monthly Revenue"
          value={`৳${avgRev.toLocaleString()}`}
          subtitle="Monthly run-rate benchmark"
          icon={TrendingUp}
          badgeText="Consistent"
          badgeColor="badge-info"
        />
        <StatCard
          title="Peak Revenue Month"
          value={maxRevMonth ? maxRevMonth.month : 'July'}
          subtitle={`Highest revenue: ৳${maxRevMonth ? maxRevMonth.revenue.toLocaleString() : '0'}`}
          icon={Award}
          badgeText="Record Sales"
          badgeColor="badge-warning"
        />
      </div>

      {/* Grid 1: Revenue & Popular Products */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Monthly Revenue Chart */}
        <div className="bg-white rounded-2xl border border-slate-200 p-5 shadow-2xs space-y-3">
          <div>
            <h3 className="font-bold text-slate-900 text-base">Monthly Revenue Report</h3>
            <p className="text-xs text-slate-500">Track month-by-month sales performance</p>
          </div>
          <RevenueChart data={mockMonthlyRevenue} />
        </div>

        {/* Popular Products Chart */}
        <div className="bg-white rounded-2xl border border-slate-200 p-5 shadow-2xs space-y-3">
          <div>
            <h3 className="font-bold text-slate-900 text-base">Popular Products (Top Sellers)</h3>
            <p className="text-xs text-slate-500">Product sales quantity leaderboard ranking</p>
          </div>
          <ProductPopularityChart data={mockPopularProducts} />
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
          <RatingChart />
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
            <p className="text-xs text-slate-500">Conceptual getSeasonalTrend(Product) data</p>
          </div>
          <SeasonalTrendChart data={mockSeasonalTrends} />
        </div>
      </div>
    </div>
  );
};

export default Reports;
