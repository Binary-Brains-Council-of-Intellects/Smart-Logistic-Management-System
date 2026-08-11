import React from 'react';
import { PackageOpen } from 'lucide-react';

const EmptyState = ({ title = 'No records found', message = 'Try adjusting your search query or filters.', onReset }) => {
  return (
    <div className="flex flex-col items-center justify-center p-12 text-center bg-white rounded-2xl border border-slate-200">
      <div className="p-4 bg-slate-100 rounded-full text-slate-400 mb-3">
        <PackageOpen className="w-8 h-8 stroke-[1.5]" />
      </div>
      <h4 className="text-base font-bold text-slate-800">{title}</h4>
      <p className="text-xs text-slate-500 max-w-sm mt-1 mb-4">{message}</p>
      {onReset && (
        <button
          onClick={onReset}
          className="btn btn-sm btn-outline border-slate-300 text-slate-700 hover:bg-slate-100 rounded-xl"
        >
          Reset Filters
        </button>
      )}
    </div>
  );
};

export default EmptyState;
