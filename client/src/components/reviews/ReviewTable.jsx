import React from 'react';
import { Star } from 'lucide-react';

const ReviewTable = ({ reviews }) => {
  const renderStars = (rating) => {
    const stars = [];
    for (let i = 1; i <= 5; i++) {
      stars.push(
        <Star
          key={i}
          className={`w-4 h-4 ${i <= rating ? 'text-amber-400 fill-amber-400' : 'text-slate-300'}`}
        />
      );
    }
    return <div className="flex items-center gap-0.5">{stars}</div>;
  };

  return (
    <div className="overflow-x-auto bg-white rounded-2xl border border-slate-200 shadow-2xs">
      <table className="table table-sm sm:table-md w-full">
        <thead>
          <tr className="bg-slate-50 text-slate-600 text-xs uppercase tracking-wider border-b border-slate-200">
            <th className="py-3.5 px-4 font-bold">Review ID</th>
            <th className="py-3.5 px-4 font-bold">Product Name</th>
            <th className="py-3.5 px-4 font-bold">Customer</th>
            <th className="py-3.5 px-4 font-bold">Rating</th>
            <th className="py-3.5 px-4 font-bold">Comment / Feedback</th>
            <th className="py-3.5 px-4 font-bold">Date</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-slate-100 text-sm text-slate-800 font-medium">
          {reviews.map((rev) => (
            <tr key={rev.id} className="hover:bg-slate-50/80 transition-colors">
              <td className="px-4 py-3.5 font-mono text-xs text-slate-500 font-semibold">{rev.id}</td>
              <td className="px-4 py-3.5 font-bold text-slate-900">{rev.productName}</td>
              <td className="px-4 py-3.5 text-xs text-slate-700">{rev.customerName}</td>
              <td className="px-4 py-3.5">{renderStars(rev.rating)}</td>
              <td className="px-4 py-3.5 text-xs text-slate-600 max-w-xs truncate">{rev.comment}</td>
              <td className="px-4 py-3.5 text-xs text-slate-500">{rev.reviewDate}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ReviewTable;
