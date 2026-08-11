import React, { useState } from 'react';
import Modal from '../common/Modal';
import { Star } from 'lucide-react';
import { useSLMS } from '../../context/SLMSContext';

const ReviewFormModal = ({ isOpen, onClose, onSave }) => {
  const { products } = useSLMS();

  const [productId, setProductId] = useState(products[0]?.id || '');
  const [customerName, setCustomerName] = useState('');
  const [rating, setRating] = useState(5);
  const [reviewDate, setReviewDate] = useState(new Date().toISOString().split('T')[0]);
  const [comment, setComment] = useState('');
  const [errors, setErrors] = useState({});

  const validate = () => {
    const err = {};
    if (!customerName.trim()) err.customerName = 'Customer name is required';
    if (!comment.trim()) err.comment = 'Feedback comment is required';
    setErrors(err);
    return Object.keys(err).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validate()) {
      const selectedProd = products.find((p) => p.id === productId);
      onSave({
        productId,
        productName: selectedProd ? selectedProd.name : 'Product',
        customerName,
        rating,
        reviewDate,
        comment
      });
      setCustomerName('');
      setComment('');
      onClose();
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Submit Customer Product Review">
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="label text-xs font-bold text-slate-700">Select Product *</label>
          <select
            value={productId}
            onChange={(e) => setProductId(e.target.value)}
            className="select select-sm border-slate-200 w-full text-xs"
          >
            {products.map((p) => (
              <option key={p.id} value={p.id}>
                {p.name} ({p.id})
              </option>
            ))}
          </select>
        </div>

        <div>
          <label className="label text-xs font-bold text-slate-700">Customer Name *</label>
          <input
            type="text"
            placeholder="e.g. Tariqul Islam"
            value={customerName}
            onChange={(e) => setCustomerName(e.target.value)}
            className={`input input-sm border-slate-200 w-full text-xs ${errors.customerName ? 'input-error' : ''}`}
          />
          {errors.customerName && (
            <span className="text-[10px] text-rose-600 font-semibold">{errors.customerName}</span>
          )}
        </div>

        <div>
          <label className="label text-xs font-bold text-slate-700">Rating (1 to 5 Stars) *</label>
          <div className="flex items-center gap-2 p-2 bg-slate-50 rounded-xl border border-slate-200">
            {[1, 2, 3, 4, 5].map((star) => (
              <button
                key={star}
                type="button"
                onClick={() => setRating(star)}
                className="p-1 hover:scale-110 transition-transform"
              >
                <Star
                  className={`w-6 h-6 ${
                    star <= rating ? 'text-amber-400 fill-amber-400' : 'text-slate-300'
                  }`}
                />
              </button>
            ))}
            <span className="text-xs font-bold text-slate-700 ml-2">{rating} Star{rating > 1 ? 's' : ''}</span>
          </div>
        </div>

        <div>
          <label className="label text-xs font-bold text-slate-700">Review Date</label>
          <input
            type="date"
            value={reviewDate}
            onChange={(e) => setReviewDate(e.target.value)}
            className="input input-sm border-slate-200 w-full text-xs"
          />
        </div>

        <div>
          <label className="label text-xs font-bold text-slate-700">Comment / Review text *</label>
          <textarea
            rows="3"
            placeholder="Enter customer feedback notes..."
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            className={`textarea textarea-sm border-slate-200 w-full text-xs ${errors.comment ? 'textarea-error' : ''}`}
          ></textarea>
          {errors.comment && <span className="text-[10px] text-rose-600 font-semibold">{errors.comment}</span>}
        </div>

        <div className="flex items-center justify-end gap-2 pt-3 border-t border-slate-200">
          <button type="button" onClick={onClose} className="btn btn-sm btn-ghost text-slate-600 rounded-xl">
            Cancel
          </button>
          <button type="submit" className="btn btn-sm bg-blue-600 hover:bg-blue-700 text-white rounded-xl px-5 border-0">
            Submit Review
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default ReviewFormModal;
