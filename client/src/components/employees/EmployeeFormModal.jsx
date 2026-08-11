import React, { useState, useEffect } from 'react';
import Modal from '../common/Modal';

const EmployeeFormModal = ({ isOpen, onClose, onSave, initialData }) => {
  const [formData, setFormData] = useState({
    id: '',
    name: '',
    designation: '',
    hourlyRate: 200,
    status: 'ACTIVE'
  });

  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (initialData) {
      setFormData(initialData);
    } else {
      setFormData({
        id: `EMP-${Math.floor(200 + Math.random() * 800)}`,
        name: '',
        designation: 'Warehouse Operator',
        hourlyRate: 200,
        status: 'ACTIVE'
      });
    }
    setErrors({});
  }, [initialData, isOpen]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const validate = () => {
    const err = {};
    if (!formData.name.trim()) err.name = 'Employee name is required';
    if (!formData.designation.trim()) err.designation = 'Designation is required';
    if (Number(formData.hourlyRate) <= 0) err.hourlyRate = 'Hourly rate must be > 0';
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
    <Modal isOpen={isOpen} onClose={onClose} title={initialData ? 'Edit Employee Record' : 'Add New Employee'}>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="label text-xs font-bold text-slate-700">Employee ID</label>
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
          <label className="label text-xs font-bold text-slate-700">Full Name *</label>
          <input
            type="text"
            name="name"
            placeholder="e.g. Rahim Ahmed"
            value={formData.name}
            onChange={handleChange}
            className={`input input-sm border-slate-200 w-full text-xs ${errors.name ? 'input-error' : ''}`}
          />
          {errors.name && <span className="text-[10px] text-rose-600 font-semibold">{errors.name}</span>}
        </div>

        <div>
          <label className="label text-xs font-bold text-slate-700">Designation *</label>
          <input
            type="text"
            name="designation"
            placeholder="e.g. Warehouse Supervisor"
            value={formData.designation}
            onChange={handleChange}
            className={`input input-sm border-slate-200 w-full text-xs ${errors.designation ? 'input-error' : ''}`}
          />
          {errors.designation && (
            <span className="text-[10px] text-rose-600 font-semibold">{errors.designation}</span>
          )}
        </div>

        <div>
          <label className="label text-xs font-bold text-slate-700">Hourly Rate (৳) *</label>
          <input
            type="number"
            name="hourlyRate"
            min="10"
            value={formData.hourlyRate}
            onChange={handleChange}
            className={`input input-sm border-slate-200 w-full text-xs ${errors.hourlyRate ? 'input-error' : ''}`}
          />
          {errors.hourlyRate && (
            <span className="text-[10px] text-rose-600 font-semibold">{errors.hourlyRate}</span>
          )}
        </div>

        <div>
          <label className="label text-xs font-bold text-slate-700">Employment Status</label>
          <select
            name="status"
            value={formData.status}
            onChange={handleChange}
            className="select select-sm border-slate-200 w-full text-xs"
          >
            <option value="ACTIVE">ACTIVE</option>
            <option value="INACTIVE">INACTIVE</option>
          </select>
        </div>

        <div className="flex items-center justify-end gap-2 pt-4 border-t border-slate-200">
          <button type="button" onClick={onClose} className="btn btn-sm btn-ghost text-slate-600 rounded-xl">
            Cancel
          </button>
          <button type="submit" className="btn btn-sm bg-blue-600 hover:bg-blue-700 text-white rounded-xl px-5 border-0">
            Save Employee
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default EmployeeFormModal;
