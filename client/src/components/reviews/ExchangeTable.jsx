import React from 'react';
import StatusBadge from '../common/StatusBadge';

const ExchangeTable = ({ exchanges }) => {
  return (
    <div className="overflow-x-auto bg-white rounded-2xl border border-slate-200 shadow-2xs">
      <table className="table table-sm sm:table-md w-full">
        <thead>
          <tr className="bg-slate-50 text-slate-600 text-xs uppercase tracking-wider border-b border-slate-200">
            <th className="py-3.5 px-4 font-bold">Return ID</th>
            <th className="py-3.5 px-4 font-bold">Order ID</th>
            <th className="py-3.5 px-4 font-bold">Product</th>
            <th className="py-3.5 px-4 font-bold">Customer</th>
            <th className="py-3.5 px-4 font-bold">Return Reason</th>
            <th className="py-3.5 px-4 font-bold">Date</th>
            <th className="py-3.5 px-4 font-bold">Status</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-slate-100 text-sm text-slate-800 font-medium">
          {exchanges.map((exc) => (
            <tr key={exc.id} className="hover:bg-slate-50/80 transition-colors">
              <td className="px-4 py-3.5 font-mono text-xs text-slate-500 font-semibold">{exc.id}</td>
              <td className="px-4 py-3.5 font-mono text-xs text-blue-600 font-bold">{exc.orderId}</td>
              <td className="px-4 py-3.5 font-bold text-slate-900">{exc.productName}</td>
              <td className="px-4 py-3.5 text-xs text-slate-700">{exc.customerName}</td>
              <td className="px-4 py-3.5">
                <StatusBadge status={exc.reason} />
              </td>
              <td className="px-4 py-3.5 text-xs text-slate-500">{exc.exchangeDate}</td>
              <td className="px-4 py-3.5">
                <span
                  className={`badge badge-sm py-2 px-2.5 rounded-lg text-[11px] font-bold ${
                    exc.status === 'Resolved' ? 'bg-emerald-100 text-emerald-800 border-emerald-200' : 'bg-amber-100 text-amber-800 border-amber-200'
                  }`}
                >
                  {exc.status}
                </span>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ExchangeTable;
