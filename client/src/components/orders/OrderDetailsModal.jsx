import React from 'react';
import Modal from '../common/Modal';
import StatusBadge from '../common/StatusBadge';
import { XCircle } from 'lucide-react';
import { useSLMS } from '../../context/SLMSContext';

const OrderDetailsModal = ({ isOpen, onClose, order }) => {
  const { products } = useSLMS();

  if (!order) return null;

  const rawItems = Array.isArray(order.items) ? order.items : [];
  
  const displayItems = rawItems.map((item) => {
    const matchedProd = products.find(
      (p) => String(p.id || p.productId) === String(item.productId || item.id)
    );

    const productName = item.productName && item.productName !== 'Product' && item.productName !== 'Product Item'
      ? item.productName
      : (matchedProd ? matchedProd.name : (item.name || 'Product Item'));

    const quantity = Math.max(1, Number(item.quantity || 1));

    const unitPrice = Number(
      item.unitPrice !== undefined && item.unitPrice > 0
        ? item.unitPrice
        : (matchedProd ? matchedProd.sellingPrice : (item.price || 0))
    );

    const subtotal = Number(
      item.subtotal !== undefined && item.subtotal > 0
        ? item.subtotal
        : (quantity * unitPrice)
    );

    return {
      ...item,
      productName,
      quantity,
      unitPrice,
      subtotal
    };
  });

  const calculatedGrandTotal = displayItems.length > 0
    ? displayItems.reduce((acc, item) => acc + item.subtotal, 0)
    : Number(order.totalAmount || 0);

  const totalItemsCount = displayItems.length > 0
    ? displayItems.reduce((acc, item) => acc + item.quantity, 0)
    : Number(order.itemCount || 0);

  const steps = [
    { key: 'PENDING', label: 'Pending' },
    { key: 'CONFIRMED', label: 'Confirmed' },
    { key: 'DISPATCHED', label: 'Dispatched' }
  ];

  const getStepIndex = (status) => {
    if (status === 'PENDING') return 0;
    if (status === 'CONFIRMED') return 1;
    if (status === 'DISPATCHED') return 2;
    return -1;
  };

  const currentIndex = getStepIndex(order.status);

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Customer Order Details" maxWidth="max-w-2xl">
      <div className="space-y-6">
        <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between p-4 bg-slate-50 rounded-xl border border-slate-200 gap-3">
          <div>
            <div className="flex items-center gap-2 mb-1">
              <span className="font-mono text-sm font-bold text-blue-600 bg-blue-50 px-2.5 py-0.5 rounded-md border border-blue-200">
                {order.id}
              </span>
              <StatusBadge status={order.status} />
            </div>
            <h4 className="text-lg font-bold text-slate-900">{order.customerName || 'Walk-in Customer'}</h4>
            <p className="text-xs text-slate-500">Order Placed Date: {order.orderDate}</p>
          </div>
          <div className="sm:text-right">
            <span className="text-xs text-slate-400 font-semibold uppercase block">Grand Total</span>
            <span className="text-2xl font-extrabold text-slate-900">৳{calculatedGrandTotal.toLocaleString()}</span>
          </div>
        </div>

        <div className="p-4 bg-white rounded-xl border border-slate-200">
          <h5 className="text-xs font-bold uppercase tracking-wider text-slate-500 mb-3">Order Lifecycle Status</h5>
          {order.status === 'CANCELLED' ? (
            <div className="flex items-center gap-2 p-3 bg-rose-50 border border-rose-200 text-rose-700 rounded-xl text-xs font-bold">
              <XCircle className="w-5 h-5 text-rose-600" />
              <span>Order has been cancelled and stock returned to inventory.</span>
            </div>
          ) : (
            <div className="flex items-center justify-between relative px-2">
              {steps.map((step, idx) => {
                const isPassed = idx <= currentIndex;
                const isCurrent = idx === currentIndex;

                return (
                  <React.Fragment key={step.key}>
                    <div className="flex flex-col items-center gap-1.5 z-10">
                      <div
                        className={`w-8 h-8 rounded-full flex items-center justify-center font-bold text-xs transition-colors ${
                          isPassed
                            ? 'bg-blue-600 text-white ring-4 ring-blue-100'
                            : 'bg-slate-100 text-slate-400 border border-slate-200'
                        }`}
                      >
                        {idx + 1}
                      </div>
                      <span className={`text-xs font-bold ${isCurrent ? 'text-blue-600' : isPassed ? 'text-slate-900' : 'text-slate-400'}`}>
                        {step.label}
                      </span>
                    </div>
                    {idx < steps.length - 1 && (
                      <div className={`flex-1 h-1 mx-2 rounded-full ${idx < currentIndex ? 'bg-blue-600' : 'bg-slate-200'}`} />
                    )}
                  </React.Fragment>
                );
              })}
            </div>
          )}
        </div>

        <div className="space-y-2">
          <h5 className="text-xs font-bold uppercase tracking-wider text-slate-600">Itemized Breakdown</h5>
          <div className="overflow-x-auto rounded-xl border border-slate-200">
            <table className="table table-sm w-full">
              <thead>
                <tr className="bg-slate-50 text-slate-500 text-xs">
                  <th className="py-2.5 px-3">Product</th>
                  <th className="py-2.5 px-3">Quantity</th>
                  <th className="py-2.5 px-3">Unit Price</th>
                  <th className="py-2.5 px-3 text-right">Subtotal</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 text-xs font-medium">
                {displayItems.length === 0 ? (
                  <tr>
                    <td colSpan="4" className="py-4 text-center text-slate-500">No item details available for this order.</td>
                  </tr>
                ) : (
                  displayItems.map((item, idx) => (
                    <tr key={idx}>
                      <td className="py-2.5 px-3 font-bold text-slate-900">{item.productName}</td>
                      <td className="py-2.5 px-3 font-semibold text-slate-700">{item.quantity} units</td>
                      <td className="py-2.5 px-3 text-slate-600">৳{item.unitPrice.toLocaleString()}</td>
                      <td className="py-2.5 px-3 text-right font-extrabold text-slate-900">৳{item.subtotal.toLocaleString()}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>

        <div className="flex justify-between items-center text-xs pt-2 border-t border-slate-200 font-semibold text-slate-600">
          <span>Total Items Ordered: {totalItemsCount} {totalItemsCount === 1 ? 'Item' : 'Items'}</span>
          <button onClick={onClose} className="btn btn-sm btn-outline border-slate-300 text-slate-700 rounded-xl">
            Close View
          </button>
        </div>
      </div>
    </Modal>
  );
};

export default OrderDetailsModal;
