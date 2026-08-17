import React, { useState, useEffect } from 'react';
import Modal from '../common/Modal';
import { useSLMS } from '../../context/SLMSContext';

const ExchangeFormModal = ({ isOpen, onClose, onSave }) => {
  const { products } = useSLMS();

  const [orderId, setOrderId] = useState(`ORD-${Math.floor(9000 + Math.random() * 1000)}`);
  const [productId, setProductId] = useState(products[0]?.id || '');
  const [customerName, setCustomerName] = useState('');
  const [reason, setReason] = useState('DAMAGED');
  const [exchangeDate, setExchangeDate] = useState(new Date().toISOString().split('T')[0]);
  const [notes, setNotes] = useState('');
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (products && products.length > 0 && !productId) {
      setProductId(products[0].id || products[0].productId || '');
    }
  }, [products, productId]);

  const validate = () => {
    const err = {};
    if (!customerName.trim()) err.customerName = 'Customer name is required';
    setErrors(err);
    return Object.keys(err).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validate()) {
      const activeProdId = productId || (products[0]?.id || products[0]?.productId || '');
      const selectedProd = products.find(
        (p) => String(p.id || p.productId) === String(activeProdId)
      );

      onSave({
        orderId: orderId.trim() || `ORD-${Math.floor(9000 + Math.random() * 1000)}`,
        productId: activeProdId,
        productName: selectedProd ? selectedProd.name : 'Product Item',
        customerName: customerName.trim(),
        reason: reason || 'DAMAGED',
        exchangeDate: exchangeDate || new Date().toISOString().split('T')[0],
        status: 'Pending Inspection',
        notes: notes.trim()
      });

      setCustomerName('');
      setNotes('');
      setErrors({});
      onClose();
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Record Product Return / Exchange">
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="label text-xs font-bold text-slate-700">Order ID *</label>
          <input
            type="text"
            placeholder="e.g. ORD-9001"
            value={orderId}
            onChange={(e) => setOrderId(e.target.value)}
            className="input input-sm border-slate-200 w-full font-mono text-xs"
          />
        </div>

        <div>
          <label className="label text-xs font-bold text-slate-700">Select Returned Product *</label>
          <select
            value={productId}
            onChange={(e) => setProductId(e.target.value)}
            className="select select-sm border-slate-200 w-full text-xs"
          >
            {products.map((p) => (
              <option key={p.id || p.productId} value={p.id || p.productId}>
                {p.name} ({p.id || p.productId})
              </option>
            ))}
          </select>
        </div>

        <div>
          <label className="label text-xs font-bold text-slate-700">Customer Name *</label>
          <input
            type="text"
            placeholder="e.g. Apex Retailers Ltd."
            value={customerName}
            onChange={(e) => setCustomerName(e.target.value)}
            className={`input input-sm border-slate-200 w-full text-xs ${errors.customerName ? 'input-error' : ''}`}
          />
          {errors.customerName && (
            <span className="text-[10px] text-rose-600 font-semibold">{errors.customerName}</span>
          )}
        </div>

        <div>
          <label className="label text-xs font-bold text-slate-700">Return Reason *</label>
          <select
            value={reason}
            onChange={(e) => setReason(e.target.value)}
            className="select select-sm border-slate-200 w-full text-xs font-semibold"
          >
            <option value="WRONG_PRODUCT">Wrong Product Delivered</option>
            <option value="DAMAGED">Damaged Package / Defective</option>
            <option value="EXPIRED_ON_ARRIVAL">Expired on Arrival</option>
            <option value="OTHER">Other Reason</option>
          </select>
        </div>

        <div>
          <label className="label text-xs font-bold text-slate-700">Return Date</label>
          <input
            type="date"
            value={exchangeDate}
            onChange={(e) => setExchangeDate(e.target.value)}
            className="input input-sm border-slate-200 w-full text-xs"
          />
        </div>

        <div>
          <label className="label text-xs font-bold text-slate-700">Inspection & Resolution Notes</label>
          <textarea
            rows="2"
            placeholder="Enter reason details or replacement shipment notes..."
            value={notes}
            onChange={(e) => setNotes(e.target.value)}
            className="textarea textarea-sm border-slate-200 w-full text-xs"
          ></textarea>
        </div>

        <div className="flex items-center justify-end gap-2 pt-3 border-t border-slate-200">
          <button type="button" onClick={onClose} className="btn btn-sm btn-ghost text-slate-600 rounded-xl">
            Cancel
          </button>
          <button type="submit" className="btn btn-sm bg-blue-600 hover:bg-blue-700 text-white rounded-xl px-5 border-0">
            Record Return
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default ExchangeFormModal;
