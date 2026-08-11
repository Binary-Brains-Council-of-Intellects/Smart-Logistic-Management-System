import React, { useState } from 'react';
import PageHeader from '../components/common/PageHeader';
import OrderTable from '../components/orders/OrderTable';
import OrderFormModal from '../components/orders/OrderFormModal';
import OrderDetailsModal from '../components/orders/OrderDetailsModal';
import EmptyState from '../components/common/EmptyState';
import { Plus, Search, RefreshCw } from 'lucide-react';
import { useSLMS } from '../context/SLMSContext';

const Orders = () => {
  const { orders, createOrder, updateOrderStatus } = useSLMS();

  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [dateFilter, setDateFilter] = useState('');

  // Modal controls
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedOrderDetails, setSelectedOrderDetails] = useState(null);

  const handleResetFilters = () => {
    setSearchTerm('');
    setStatusFilter('');
    setDateFilter('');
  };

  const filteredOrders = orders.filter((o) => {
    const matchesSearch =
      o.id.toLowerCase().includes(searchTerm.toLowerCase()) ||
      o.customerName.toLowerCase().includes(searchTerm.toLowerCase());

    const matchesStatus = statusFilter ? o.status === statusFilter : true;
    const matchesDate = dateFilter ? o.orderDate === dateFilter : true;

    return matchesSearch && matchesStatus && matchesDate;
  });

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <PageHeader
        title="Customer Orders"
        subtitle="Manage customer orders, stock allocation, and sales fulfillment."
      >
        <button
          onClick={() => setIsFormOpen(true)}
          className="btn btn-sm bg-blue-600 hover:bg-blue-700 text-white rounded-xl gap-1.5 border-0 shadow-sm"
        >
          <Plus className="w-4 h-4" />
          <span>Create Order</span>
        </button>
      </PageHeader>

      {/* Filter Bar */}
      <div className="bg-white rounded-2xl border border-slate-200 p-4 shadow-2xs space-y-3">
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
          {/* Search Box */}
          <div className="relative">
            <input
              type="text"
              placeholder="Search Order ID or Customer..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="input input-sm border-slate-200 w-full pl-9 text-xs rounded-xl focus:ring-2 focus:ring-blue-500/20"
            />
            <Search className="w-4 h-4 text-slate-400 absolute left-3 top-2.5" />
          </div>

          {/* Status Filter */}
          <div>
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="select select-sm border-slate-200 w-full text-xs rounded-xl"
            >
              <option value="">All Lifecycle Statuses</option>
              <option value="PENDING">Pending Confirmation</option>
              <option value="CONFIRMED">Confirmed</option>
              <option value="DISPATCHED">Dispatched</option>
              <option value="CANCELLED">Cancelled</option>
            </select>
          </div>

          {/* Date Filter */}
          <div>
            <input
              type="date"
              value={dateFilter}
              onChange={(e) => setDateFilter(e.target.value)}
              className="input input-sm border-slate-200 w-full text-xs rounded-xl"
            />
          </div>
        </div>

        {(searchTerm || statusFilter || dateFilter) && (
          <div className="flex items-center justify-between pt-2 border-t border-slate-100 text-xs">
            <span className="text-slate-500 font-medium">
              Showing {filteredOrders.length} of {orders.length} orders
            </span>
            <button
              onClick={handleResetFilters}
              className="text-blue-600 hover:underline font-bold flex items-center gap-1"
            >
              <RefreshCw className="w-3 h-3" /> Reset Filters
            </button>
          </div>
        )}
      </div>

      {/* Main Table or Empty State */}
      {filteredOrders.length === 0 ? (
        <EmptyState
          title="No customer orders found"
          message="Try changing search criteria or create a new order."
          onReset={handleResetFilters}
        />
      ) : (
        <OrderTable
          orders={filteredOrders}
          onViewDetails={(o) => setSelectedOrderDetails(o)}
          onUpdateStatus={(id, status) => updateOrderStatus(id, status)}
        />
      )}

      {/* Modals */}
      <OrderFormModal
        isOpen={isFormOpen}
        onClose={() => setIsFormOpen(false)}
        onSave={(data) => createOrder(data)}
      />

      <OrderDetailsModal
        isOpen={!!selectedOrderDetails}
        onClose={() => setSelectedOrderDetails(null)}
        order={selectedOrderDetails}
      />
    </div>
  );
};

export default Orders;
