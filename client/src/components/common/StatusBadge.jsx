import React from 'react';

const StatusBadge = ({ status }) => {
  const normalized = String(status || '').toUpperCase();

  let colorClass = 'badge-neutral';
  let label = status;

  switch (normalized) {
    // Stock / Product Statuses
    case 'IN STOCK':
    case 'IN_STOCK':
      colorClass = 'badge-success text-emerald-950 font-bold';
      label = 'In Stock';
      break;
    case 'LOW STOCK':
    case 'LOW_STOCK':
      colorClass = 'badge-warning text-amber-950 font-bold';
      label = 'Low Stock';
      break;
    case 'OUT OF STOCK':
    case 'OUT_OF_STOCK':
      colorClass = 'badge-error text-rose-950 font-bold';
      label = 'Out of Stock';
      break;

    // Expiry Statuses
    case 'VALID':
      colorClass = 'badge-success text-emerald-950 font-bold';
      label = 'Valid';
      break;
    case 'NEAR EXPIRY':
    case 'NEAR_EXPIRY':
      colorClass = 'badge-warning text-amber-950 font-bold';
      label = 'Near Expiry';
      break;
    case 'EXPIRED':
      colorClass = 'badge-error text-rose-950 font-bold';
      label = 'Expired';
      break;

    // Order Lifecycle Statuses
    case 'PENDING':
      colorClass = 'badge-warning text-amber-950 font-bold';
      label = 'Pending';
      break;
    case 'CONFIRMED':
      colorClass = 'badge-success text-emerald-950 font-bold';
      label = 'Confirmed';
      break;
    case 'DISPATCHED':
      colorClass = 'badge-info text-sky-950 font-bold';
      label = 'Dispatched';
      break;
    case 'CANCELLED':
      colorClass = 'badge-error text-rose-950 font-bold';
      label = 'Cancelled';
      break;

    // Employee Statuses
    case 'ACTIVE':
      colorClass = 'badge-success text-emerald-950 font-bold';
      label = 'Active';
      break;
    case 'INACTIVE':
      colorClass = 'badge-ghost text-slate-600 font-bold';
      label = 'Inactive';
      break;

    // Exchange Reason Statuses
    case 'WRONG_PRODUCT':
      colorClass = 'badge-warning text-amber-950 font-bold';
      label = 'Wrong Product';
      break;
    case 'DAMAGED':
      colorClass = 'badge-error text-rose-950 font-bold';
      label = 'Damaged';
      break;
    case 'EXPIRED_ON_ARRIVAL':
      colorClass = 'badge-error text-rose-950 font-bold';
      label = 'Expired on Arrival';
      break;

    default:
      label = status;
      break;
  }

  return (
    <span className={`badge badge-sm py-2 px-2.5 rounded-lg text-[11px] uppercase tracking-wide border-0 ${colorClass}`}>
      {label}
    </span>
  );
};

export default StatusBadge;
