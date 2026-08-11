import React from 'react';
import { Eye, CheckCircle2, Truck, XCircle, MoreVertical } from 'lucide-react';
import StatusBadge from '../common/StatusBadge';

const OrderTable = ({ orders, onViewDetails, onUpdateStatus }) => {
  return (
    <div className="overflow-x-auto bg-white rounded-2xl border border-slate-200 shadow-2xs">
      <table className="table table-sm sm:table-md w-full">
        <thead>
          <tr className="bg-slate-50 text-slate-600 text-xs uppercase tracking-wider border-b border-slate-200">
            <th className="py-3.5 px-4 font-bold">Order ID</th>
            <th className="py-3.5 px-4 font-bold">Customer Name</th>
            <th className="py-3.5 px-4 font-bold">Order Date</th>
            <th className="py-3.5 px-4 font-bold">Items Count</th>
            <th className="py-3.5 px-4 font-bold">Total Amount</th>
            <th className="py-3.5 px-4 font-bold">Status</th>
            <th className="py-3.5 px-4 font-bold text-right">Actions</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-slate-100 text-sm text-slate-800 font-medium">
          {orders.map((o) => {
            const itemCount = o.items ? o.items.reduce((sum, item) => sum + Number(item.quantity || 1), 0) : 0;

            return (
              <tr key={o.id} className="hover:bg-slate-50/80 transition-colors">
                <td className="px-4 py-3.5 font-mono text-xs text-blue-600 font-bold">{o.id}</td>
                <td className="px-4 py-3.5 font-bold text-slate-900">{o.customerName}</td>
                <td className="px-4 py-3.5 text-xs text-slate-500">{o.orderDate}</td>
                <td className="px-4 py-3.5">
                  <span className="px-2 py-0.5 rounded-md bg-slate-100 text-slate-700 text-xs font-semibold">
                    {itemCount} units ({o.items ? o.items.length : 0} lines)
                  </span>
                </td>
                <td className="px-4 py-3.5 font-extrabold text-slate-900">৳{o.totalAmount.toLocaleString()}</td>
                <td className="px-4 py-3.5">
                  <StatusBadge status={o.status} />
                </td>
                <td className="px-4 py-3.5 text-right">
                  <div className="dropdown dropdown-end">
                    <div tabIndex={0} role="button" className="btn btn-xs btn-ghost btn-circle text-slate-500">
                      <MoreVertical className="w-4 h-4" />
                    </div>
                    <ul tabIndex={0} className="dropdown-content z-10 menu p-1 shadow-lg bg-white rounded-xl w-40 border border-slate-100 text-xs">
                      <li>
                        <button onClick={() => onViewDetails(o)} className="flex items-center gap-2 py-2">
                          <Eye className="w-3.5 h-3.5 text-blue-600" /> View Order
                        </button>
                      </li>

                      {o.status === 'PENDING' && (
                        <>
                          <li>
                            <button
                              onClick={() => onUpdateStatus(o.id, 'CONFIRMED')}
                              className="flex items-center gap-2 py-2 text-emerald-600 font-semibold"
                            >
                              <CheckCircle2 className="w-3.5 h-3.5" /> Confirm Order
                            </button>
                          </li>
                          <li>
                            <button
                              onClick={() => onUpdateStatus(o.id, 'CANCELLED')}
                              className="flex items-center gap-2 py-2 text-rose-600"
                            >
                              <XCircle className="w-3.5 h-3.5" /> Cancel Order
                            </button>
                          </li>
                        </>
                      )}

                      {o.status === 'CONFIRMED' && (
                        <>
                          <li>
                            <button
                              onClick={() => onUpdateStatus(o.id, 'DISPATCHED')}
                              className="flex items-center gap-2 py-2 text-sky-600 font-semibold"
                            >
                              <Truck className="w-3.5 h-3.5" /> Dispatch Order
                            </button>
                          </li>
                          <li>
                            <button
                              onClick={() => onUpdateStatus(o.id, 'CANCELLED')}
                              className="flex items-center gap-2 py-2 text-rose-600"
                            >
                              <XCircle className="w-3.5 h-3.5" /> Cancel Order
                            </button>
                          </li>
                        </>
                      )}
                    </ul>
                  </div>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default OrderTable;
