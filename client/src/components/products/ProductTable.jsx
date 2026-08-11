import React from 'react';
import { Eye, Edit3, Trash2, MoreVertical } from 'lucide-react';
import StatusBadge from '../common/StatusBadge';

const ProductTable = ({ products, onViewDetails, onEdit, onDelete }) => {
  const getStockStatus = (p) => {
    const avail = Number(p.availableQuantity);
    if (avail === 0) return 'OUT_OF_STOCK';
    if (avail <= 10) return 'LOW_STOCK';
    return 'IN_STOCK';
  };

  const getExpiryStatus = (p) => {
    if (p.type !== 'PerishableProduct' || !p.expiryDate) return null;
    const today = new Date();
    const expiry = new Date(p.expiryDate);
    const diffDays = Math.ceil((expiry - today) / (1000 * 60 * 60 * 24));
    if (diffDays < 0) return 'EXPIRED';
    if (diffDays <= 30) return 'NEAR_EXPIRY';
    return 'VALID';
  };

  return (
    <div className="overflow-x-auto bg-white rounded-2xl border border-slate-200 shadow-2xs">
      <table className="table table-sm sm:table-md w-full">
        <thead>
          <tr className="bg-slate-50 text-slate-600 text-xs uppercase tracking-wider border-b border-slate-200">
            <th className="py-3.5 px-4 font-bold">Product ID</th>
            <th className="py-3.5 px-4 font-bold">Product Name</th>
            <th className="py-3.5 px-4 font-bold">Category</th>
            <th className="py-3.5 px-4 font-bold">Batch</th>
            <th className="py-3.5 px-4 font-bold">Avail. Stock</th>
            <th className="py-3.5 px-4 font-bold">Cost Price</th>
            <th className="py-3.5 px-4 font-bold">Selling Price</th>
            <th className="py-3.5 px-4 font-bold">Status</th>
            <th className="py-3.5 px-4 font-bold text-right">Actions</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-slate-100 text-sm text-slate-800 font-medium">
          {products.map((p) => {
            const stockStatus = getStockStatus(p);
            const expiryStatus = getExpiryStatus(p);

            return (
              <tr key={p.id} className="hover:bg-slate-50/80 transition-colors">
                <td className="px-4 py-3.5 font-mono text-xs text-slate-500 font-semibold">{p.id}</td>
                <td className="px-4 py-3.5 font-bold text-slate-900">{p.name}</td>
                <td className="px-4 py-3.5">
                  <span className="px-2 py-0.5 rounded-md bg-slate-100 text-slate-700 text-xs font-semibold">
                    {p.category}
                  </span>
                </td>
                <td className="px-4 py-3.5 font-mono text-xs text-slate-600">{p.batchNumber}</td>
                <td className="px-4 py-3.5">
                  <span className="font-bold text-slate-900">{p.availableQuantity}</span>
                  <span className="text-xs text-slate-400 font-normal"> / {p.totalQuantity}</span>
                </td>
                <td className="px-4 py-3.5 text-slate-700">৳{p.costPrice.toLocaleString()}</td>
                <td className="px-4 py-3.5 font-bold text-slate-900">৳{p.sellingPrice.toLocaleString()}</td>
                <td className="px-4 py-3.5 space-x-1">
                  <StatusBadge status={stockStatus} />
                  {expiryStatus && <StatusBadge status={expiryStatus} />}
                </td>
                <td className="px-4 py-3.5 text-right">
                  <div className="dropdown dropdown-end">
                    <div tabIndex={0} role="button" className="btn btn-xs btn-ghost btn-circle text-slate-500">
                      <MoreVertical className="w-4 h-4" />
                    </div>
                    <ul tabIndex={0} className="dropdown-content z-10 menu p-1 shadow-lg bg-white rounded-xl w-36 border border-slate-100 text-xs">
                      <li>
                        <button onClick={() => onViewDetails(p)} className="flex items-center gap-2 py-2">
                          <Eye className="w-3.5 h-3.5 text-blue-600" /> View Specs
                        </button>
                      </li>
                      <li>
                        <button onClick={() => onEdit(p)} className="flex items-center gap-2 py-2">
                          <Edit3 className="w-3.5 h-3.5 text-amber-600" /> Edit Product
                        </button>
                      </li>
                      <li>
                        <button onClick={() => onDelete(p.id)} className="flex items-center gap-2 py-2 text-rose-600">
                          <Trash2 className="w-3.5 h-3.5" /> Delete
                        </button>
                      </li>
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

export default ProductTable;
