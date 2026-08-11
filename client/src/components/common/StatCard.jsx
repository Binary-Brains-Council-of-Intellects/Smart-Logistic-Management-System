import React from 'react';

const StatCard = ({ title, value, subtitle, icon: Icon, badgeText, badgeColor = 'badge-ghost' }) => {
  return (
    <div className="bg-white rounded-2xl border border-slate-200 p-5 shadow-2xs hover:shadow-xs transition-shadow">
      <div className="flex items-center justify-between gap-2 mb-3">
        <span className="text-xs font-bold text-slate-500 uppercase tracking-wider">{title}</span>
        {Icon && (
          <div className="p-2 rounded-xl bg-slate-100 text-slate-700">
            <Icon className="w-5 h-5 stroke-[2]" />
          </div>
        )}
      </div>

      <div className="flex items-baseline justify-between gap-2">
        <h3 className="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">{value}</h3>
        {badgeText && <span className={`badge ${badgeColor} text-xs font-semibold px-2 py-0.5`}>{badgeText}</span>}
      </div>

      {subtitle && <p className="text-xs text-slate-500 mt-1.5 font-medium">{subtitle}</p>}
    </div>
  );
};

export default StatCard;
