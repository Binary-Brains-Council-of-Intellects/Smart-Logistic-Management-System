import React, { useState, useEffect } from 'react';
import Modal from '../common/Modal';

const ProductFormModal = ({ isOpen, onClose, onSave, initialData }) => {
  const [formData, setFormData] = useState({
    id: '',
    name: '',
    category: 'ELECTRONICS',
    type: 'NonPerishableProduct',
    batchNumber: '',
    productionDate: '',
    expiryDate: '',
    totalQuantity: 100,
    availableQuantity: 100,
    expiredQuantity: 0,
    costPrice: 0,
    sellingPrice: 0
  });

  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (initialData) {
      setFormData(initialData);
    } else {
      setFormData({
        id: `PRD-${Math.floor(1000 + Math.random() * 9000)}`,
        name: '',
        category: 'ELECTRONICS',
        type: 'NonPerishableProduct',
        batchNumber: `B2026-${Math.floor(10 + Math.random() * 90)}`,
        productionDate: new Date().toISOString().split('T')[0],
        expiryDate: '',
        totalQuantity: 100,
        availableQuantity: 100,
        expiredQuantity: 0,
        costPrice: 500,
        sellingPrice: 750
      });
    }
    setErrors({});
  }, [initialData, isOpen]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => {
      const updated = { ...prev, [name]: value };
      // When adding a new product, total quantity automatically sets available quantity
      if (name === 'totalQuantity' && !initialData) {
        updated.availableQuantity = value;
      }
      return updated;
    });
  };

  const validate = () => {
    const err = {};
    if (!formData.name.trim()) err.name = 'Product name is required';
    if (!formData.batchNumber.trim()) err.batchNumber = 'Batch number is required';
    if (Number(formData.costPrice) <= 0) err.costPrice = 'Cost price must be > 0';
    if (Number(formData.sellingPrice) <= 0) err.sellingPrice = 'Selling price must be > 0';
    if (formData.type === 'PerishableProduct' && !formData.expiryDate) {
      err.expiryDate = 'Expiry date is required for perishable products';
    }
    setErrors(err);
    return Object.keys(err).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validate()) {
      onSave(formData);
      onClose();
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title={initialData ? 'Edit Product Catalog Item' : 'Add New Inventory Product'}
    >
      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div>
            <label className="label text-xs font-bold text-slate-700">Product ID</label>
            <input
              type="text"
              name="id"
              value={formData.id}
              onChange={handleChange}
              readOnly={!!initialData}
              className="input input-sm border-slate-200 bg-slate-100 text-slate-700 w-full font-mono text-xs"
            />
          </div>

          <div>
            <label className="label text-xs font-bold text-slate-700">Product Name *</label>
            <input
              type="text"
              name="name"
              placeholder="e.g. Wireless Mouse"
              value={formData.name}
              onChange={handleChange}
              className={`input input-sm border-slate-200 w-full text-xs ${errors.name ? 'input-error' : ''}`}
            />
            {errors.name && <span className="text-[10px] text-rose-600 font-semibold">{errors.name}</span>}
          </div>

          <div>
            <label className="label text-xs font-bold text-slate-700">Category</label>
            <select
              name="category"
              value={formData.category}
              onChange={handleChange}
              className="select select-sm border-slate-200 w-full text-xs"
            >
              <option value="ELECTRONICS">Electronics</option>
              <option value="GROCERY">Grocery</option>
              <option value="PHARMACEUTICAL">Pharmaceutical</option>
              <option value="CLOTHING">Clothing</option>
              <option value="OTHER">Other</option>
            </select>
          </div>

          <div>
            <label className="label text-xs font-bold text-slate-700">Product Type</label>
            <select
              name="type"
              value={formData.type}
              onChange={handleChange}
              className="select select-sm border-slate-200 w-full text-xs"
            >
              <option value="NonPerishableProduct">Non-Perishable Product</option>
              <option value="PerishableProduct">Perishable Product</option>
            </select>
          </div>

          <div>
            <label className="label text-xs font-bold text-slate-700">Batch Number *</label>
            <input
              type="text"
              name="batchNumber"
              value={formData.batchNumber}
              onChange={handleChange}
              className={`input input-sm border-slate-200 w-full font-mono text-xs ${errors.batchNumber ? 'input-error' : ''}`}
            />
          </div>

          <div>
            <label className="label text-xs font-bold text-slate-700">Production Date</label>
            <input
              type="date"
              name="productionDate"
              value={formData.productionDate}
              onChange={handleChange}
              className="input input-sm border-slate-200 w-full text-xs"
            />
          </div>

          <div>
            <label className="label text-xs font-bold text-slate-700">
              Expiry Date {formData.type === 'PerishableProduct' ? '*' : '(Optional)'}
            </label>
            <input
              type="date"
              name="expiryDate"
              value={formData.expiryDate}
              onChange={handleChange}
              disabled={formData.type === 'NonPerishableProduct'}
              className={`input input-sm border-slate-200 w-full text-xs ${
                formData.type === 'NonPerishableProduct' ? 'bg-slate-100 cursor-not-allowed' : ''
              } ${errors.expiryDate ? 'input-error' : ''}`}
            />
            {errors.expiryDate && <span className="text-[10px] text-rose-600 font-semibold">{errors.expiryDate}</span>}
          </div>

          <div>
            <label className="label text-xs font-bold text-slate-700">Total Quantity</label>
            <input
              type="number"
              name="totalQuantity"
              min="0"
              value={formData.totalQuantity}
              onChange={handleChange}
              className="input input-sm border-slate-200 w-full text-xs"
            />
          </div>

          <div>
            <label className="label text-xs font-bold text-slate-700">Available Quantity</label>
            <input
              type="number"
              name="availableQuantity"
              min="0"
              value={formData.availableQuantity}
              onChange={handleChange}
              className="input input-sm border-slate-200 w-full text-xs"
            />
          </div>

          <div>
            <label className="label text-xs font-bold text-slate-700">Expired Quantity</label>
            <input
              type="number"
              name="expiredQuantity"
              min="0"
              value={formData.expiredQuantity}
              onChange={handleChange}
              disabled={formData.type === 'NonPerishableProduct'}
              className="input input-sm border-slate-200 w-full text-xs"
            />
          </div>

          <div>
            <label className="label text-xs font-bold text-slate-700">Cost Price (৳) *</label>
            <input
              type="number"
              name="costPrice"
              min="1"
              value={formData.costPrice}
              onChange={handleChange}
              className={`input input-sm border-slate-200 w-full text-xs ${errors.costPrice ? 'input-error' : ''}`}
            />
          </div>

          <div>
            <label className="label text-xs font-bold text-slate-700">Selling Price (৳) *</label>
            <input
              type="number"
              name="sellingPrice"
              min="1"
              value={formData.sellingPrice}
              onChange={handleChange}
              className={`input input-sm border-slate-200 w-full text-xs ${errors.sellingPrice ? 'input-error' : ''}`}
            />
          </div>
        </div>

        <div className="flex items-center justify-end gap-2 pt-4 border-t border-slate-200">
          <button
            type="button"
            onClick={onClose}
            className="btn btn-sm btn-ghost text-slate-600 rounded-xl"
          >
            Cancel
          </button>
          <button
            type="submit"
            className="btn btn-sm bg-blue-600 hover:bg-blue-700 text-white rounded-xl px-5 border-0"
          >
            Save Product
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default ProductFormModal;
