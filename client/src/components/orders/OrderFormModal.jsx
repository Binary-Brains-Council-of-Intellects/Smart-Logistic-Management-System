import React, { useState, useEffect } from 'react';
import Modal from '../common/Modal';
import { Plus, Trash2 } from 'lucide-react';
import { useSLMS } from '../../context/SLMSContext';

const OrderFormModal = ({ isOpen, onClose, onSave }) => {
  const { products } = useSLMS();

  const [customerName, setCustomerName] = useState('');
  const [orderDate, setOrderDate] = useState(new Date().toISOString().split('T')[0]);
  const [items, setItems] = useState([]);
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (isOpen) {
      setCustomerName('');
      setOrderDate(new Date().toISOString().split('T')[0]);
      if (products && products.length > 0) {
        setItems([
          {
            productId: products[0].id || products[0].productId,
            productName: products[0].name,
            availableStock: products[0].availableQuantity,
            quantity: 1,
            unitPrice: products[0].sellingPrice,
            subtotal: products[0].sellingPrice
          }
        ]);
      } else {
        setItems([]);
      }
      setErrors({});
    }
  }, [isOpen]);

  const handleProductChange = (index, prodId) => {
    const selectedProd = products.find(
      (p) => String(p.id || p.productId) === String(prodId)
    );
    if (!selectedProd) return;

    setItems((prev) =>
      prev.map((item, i) => {
        if (i === index) {
          const qty = Number(item.quantity || 1);
          const price = Number(selectedProd.sellingPrice || 0);
          return {
            ...item,
            productId: selectedProd.id || selectedProd.productId,
            productName: selectedProd.name,
            availableStock: selectedProd.availableQuantity,
            unitPrice: price,
            subtotal: qty * price
          };
        }
        return item;
      })
    );
  };

  const handleQuantityChange = (index, qty) => {
    const parsedQty = Math.max(1, Number(qty || 1));
    setItems((prev) =>
      prev.map((item, i) => {
        if (i === index) {
          return {
            ...item,
            quantity: parsedQty,
            subtotal: parsedQty * Number(item.unitPrice || 0)
          };
        }
        return item;
      })
    );
  };

  const handleAddItem = () => {
    if (!products || products.length === 0) return;
    const firstProd = products[0];
    setItems((prev) => [
      ...prev,
      {
        productId: firstProd.id || firstProd.productId,
        productName: firstProd.name,
        availableStock: firstProd.availableQuantity,
        quantity: 1,
        unitPrice: Number(firstProd.sellingPrice || 0),
        subtotal: Number(firstProd.sellingPrice || 0)
      }
    ]);
  };

  const handleRemoveItem = (index) => {
    setItems((prev) => prev.filter((_, i) => i !== index));
  };

  const orderTotal = items.reduce((sum, item) => sum + (item.subtotal || 0), 0);

  const validate = () => {
    const err = {};
    if (items.length === 0) err.items = 'At least one item is required in the order';
    setErrors(err);
    return Object.keys(err).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validate()) {
      const finalCustomerName = customerName.trim() || 'Walk-in Customer';
      const normalizedItems = items.map((item) => {
        const selectedProd = products.find(
          (p) => String(p.id || p.productId) === String(item.productId)
        );
        const qty = Math.max(1, Number(item.quantity || 1));
        const price = Number(item.unitPrice || selectedProd?.sellingPrice || 0);

        return {
          productId: item.productId || (selectedProd ? (selectedProd.id || selectedProd.productId) : 'PRD-1001'),
          productName: selectedProd ? selectedProd.name : item.productName || 'Product Item',
          quantity: qty,
          unitPrice: price,
          subtotal: qty * price
        };
      });

      const calculatedTotal = normalizedItems.reduce((sum, i) => sum + i.subtotal, 0);

      onSave({
        customerName: finalCustomerName,
        orderDate: orderDate || new Date().toISOString().split('T')[0],
        items: normalizedItems,
        totalAmount: calculatedTotal
      });

      setCustomerName('');
      setItems([]);
      setErrors({});
      onClose();
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Create New Customer Order" maxWidth="max-w-3xl">
      <form onSubmit={handleSubmit} className="space-y-6">
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
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
            <label className="label text-xs font-bold text-slate-700">Order Date</label>
            <input
              type="date"
              value={orderDate}
              onChange={(e) => setOrderDate(e.target.value)}
              className="input input-sm border-slate-200 w-full text-xs"
            />
          </div>
        </div>

        <div className="space-y-3">
          <div className="flex items-center justify-between">
            <h4 className="text-xs font-bold uppercase tracking-wider text-slate-600">Order Line Items</h4>
            <button
              type="button"
              onClick={handleAddItem}
              className="btn btn-xs bg-slate-900 text-white hover:bg-slate-800 rounded-lg gap-1"
            >
              <Plus className="w-3 h-3" /> Add Item
            </button>
          </div>

          {errors.items && <p className="text-xs text-rose-600 font-semibold">{errors.items}</p>}

          <div className="space-y-2 max-h-60 overflow-y-auto pr-1">
            {items.map((item, index) => (
              <div key={index} className="grid grid-cols-12 gap-2 items-center bg-slate-50 p-3 rounded-xl border border-slate-200 text-xs">
                <div className="col-span-5">
                  <label className="text-[10px] text-slate-500 font-semibold block mb-1">Product</label>
                  <select
                    value={item.productId}
                    onChange={(e) => handleProductChange(index, e.target.value)}
                    className="select select-xs border-slate-200 w-full text-xs"
                  >
                    {products.map((p) => (
                      <option key={p.id} value={p.id}>
                        {p.name} (Stock: {p.availableQuantity})
                      </option>
                    ))}
                  </select>
                </div>

                <div className="col-span-2">
                  <label className="text-[10px] text-slate-500 font-semibold block mb-1">Unit Price</label>
                  <span className="font-bold text-slate-800">৳{item.unitPrice}</span>
                </div>

                <div className="col-span-2">
                  <label className="text-[10px] text-slate-500 font-semibold block mb-1">Quantity</label>
                  <input
                    type="number"
                    min="1"
                    value={item.quantity}
                    onChange={(e) => handleQuantityChange(index, e.target.value)}
                    className="input input-xs border-slate-200 w-full text-xs font-bold"
                  />
                </div>

                <div className="col-span-2">
                  <label className="text-[10px] text-slate-500 font-semibold block mb-1">Subtotal</label>
                  <span className="font-extrabold text-blue-600">৳{item.subtotal.toLocaleString()}</span>
                </div>

                <div className="col-span-1 text-right">
                  <button
                    type="button"
                    onClick={() => handleRemoveItem(index)}
                    className="btn btn-xs btn-ghost btn-circle text-rose-500 hover:bg-rose-50"
                  >
                    <Trash2 className="w-3.5 h-3.5" />
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="flex items-center justify-between p-4 bg-slate-900 text-white rounded-xl">
          <div>
            <span className="text-xs text-slate-400 font-semibold block uppercase">Total Order Value</span>
            <span className="text-xs text-slate-300">{items.length} product lines included</span>
          </div>
          <span className="text-2xl font-extrabold text-emerald-400">৳{orderTotal.toLocaleString()}</span>
        </div>

        <div className="flex items-center justify-end gap-2 pt-2 border-t border-slate-200">
          <button type="button" onClick={onClose} className="btn btn-sm btn-ghost text-slate-600 rounded-xl">
            Cancel
          </button>
          <button type="submit" className="btn btn-sm bg-blue-600 hover:bg-blue-700 text-white rounded-xl px-5 border-0">
            Create Order
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default OrderFormModal;
