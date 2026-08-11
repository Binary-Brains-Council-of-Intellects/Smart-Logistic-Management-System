import React from 'react';
import Modal from '../common/Modal';
import StatusBadge from '../common/StatusBadge';
import { TrendingUp, Calendar, ShieldCheck } from 'lucide-react';

const ProductDetailsModal = ({ isOpen, onClose, product }) => {
  if (!product) return null;

  const cost = Number(product.costPrice || 0);
  const selling = Number(product.sellingPrice || 0);
  const profitMargin = cost > 0 ? (((selling - cost) / cost) * 100).toFixed(1) : '0';
  const stockPercentage =
    product.totalQuantity > 0
      ? Math.round((product.availableQuantity / product.totalQuantity) * 100)
      : 0;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Product Specification & Details" maxWidth="max-w-xl">
      <div className="space-y-6">
        <div className="bg-slate-50 p-4 rounded-xl border border-slate-200 flex items-start justify-between gap-4">
          <div>
            <div className="flex items-center gap-2 mb-1">
              <span className="font-mono text-xs font-bold text-blue-600 bg-blue-50 px-2 py-0.5 rounded-md border border-blue-200">
                {product.id}
              </span>
              <span className="text-xs font-semibold text-slate-500 uppercase">{product.category}</span>
            </div>
            <h4 className="text-xl font-bold text-slate-900">{product.name}</h4>
            <p className="text-xs text-slate-500 mt-0.5">
              Type: {product.type === 'PerishableProduct' ? 'Perishable Item' : 'Non-Perishable Item'}
            </p>
          </div>
          <div className="text-right">
            <span className="text-xs text-slate-400 font-medium block">Batch No.</span>
            <span className="font-mono text-sm font-bold text-slate-800">{product.batchNumber}</span>
          </div>
        </div>

        <div className="grid grid-cols-3 gap-3 p-4 bg-emerald-50/50 border border-emerald-200/70 rounded-xl">
          <div>
            <span className="text-xs text-emerald-800 font-semibold block">Cost Price</span>
            <span className="text-lg font-extrabold text-emerald-950">৳{cost.toLocaleString()}</span>
          </div>
          <div>
            <span className="text-xs text-emerald-800 font-semibold block">Selling Price</span>
            <span className="text-lg font-extrabold text-emerald-950">৳{selling.toLocaleString()}</span>
          </div>
          <div className="border-l border-emerald-200 pl-3">
            <span className="text-xs text-emerald-800 font-semibold flex items-center gap-1">
              <TrendingUp className="w-3.5 h-3.5 text-emerald-600" /> Profit Margin
            </span>
            <span className="text-lg font-extrabold text-emerald-600">+{profitMargin}%</span>
          </div>
        </div>

        <div className="p-4 bg-white rounded-xl border border-slate-200 space-y-2">
          <div className="flex justify-between items-center text-xs">
            <span className="font-bold text-slate-700">Stock Capacity Availability</span>
            <span className="font-bold text-slate-900">
              {product.availableQuantity} / {product.totalQuantity} Units ({stockPercentage}%)
            </span>
          </div>
          <progress
            className={`progress w-full h-2.5 ${
              stockPercentage <= 10 ? 'progress-error' : stockPercentage <= 30 ? 'progress-warning' : 'progress-primary'
            }`}
            value={product.availableQuantity}
            max={product.totalQuantity}
          ></progress>
          {product.expiredQuantity > 0 && (
            <p className="text-xs text-rose-600 font-semibold">
              ⚠️ Warning: {product.expiredQuantity} units recorded as expired/damaged.
            </p>
          )}
        </div>

        <div className="grid grid-cols-2 gap-4 text-xs">
          <div className="p-3 bg-slate-50 rounded-xl border border-slate-200 space-y-1">
            <span className="text-slate-400 font-semibold block">Production Date</span>
            <div className="flex items-center gap-1.5 font-bold text-slate-800">
              <Calendar className="w-4 h-4 text-slate-500" />
              <span>{product.productionDate || 'N/A'}</span>
            </div>
          </div>

          <div className="p-3 bg-slate-50 rounded-xl border border-slate-200 space-y-1">
            <span className="text-slate-400 font-semibold block">Expiry Date</span>
            <div className="flex items-center gap-1.5 font-bold text-slate-800">
              <ShieldCheck className="w-4 h-4 text-slate-500" />
              <span>{product.expiryDate || 'N/A (Non-Perishable)'}</span>
            </div>
          </div>
        </div>

        <div className="flex justify-end pt-2">
          <button onClick={onClose} className="btn btn-sm btn-outline border-slate-300 text-slate-700 rounded-xl">
            Close Overview
          </button>
        </div>
      </div>
    </Modal>
  );
};

export default ProductDetailsModal;
