import React from 'react';
import { Edit3, Trash2, Clock, MoreVertical } from 'lucide-react';
import StatusBadge from '../common/StatusBadge';

const EmployeeTable = ({ employees, onEdit, onDelete, onRecordAttendance }) => {
  return (
    <div className="overflow-x-auto bg-white rounded-2xl border border-slate-200 shadow-2xs">
      <table className="table table-sm sm:table-md w-full">
        <thead>
          <tr className="bg-slate-50 text-slate-600 text-xs uppercase tracking-wider border-b border-slate-200">
            <th className="py-3.5 px-4 font-bold">Employee ID</th>
            <th className="py-3.5 px-4 font-bold">Employee Name</th>
            <th className="py-3.5 px-4 font-bold">Designation</th>
            <th className="py-3.5 px-4 font-bold">Hourly Rate</th>
            <th className="py-3.5 px-4 font-bold">Status</th>
            <th className="py-3.5 px-4 font-bold text-right">Actions</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-slate-100 text-sm text-slate-800 font-medium">
          {employees.map((emp) => (
            <tr key={emp.id} className="hover:bg-slate-50/80 transition-colors">
              <td className="px-4 py-3.5 font-mono text-xs text-slate-500 font-semibold">{emp.id}</td>
              <td className="px-4 py-3.5 font-bold text-slate-900">{emp.name}</td>
              <td className="px-4 py-3.5 text-xs text-slate-600">{emp.designation}</td>
              <td className="px-4 py-3.5 font-bold text-slate-900">৳{emp.hourlyRate} / hr</td>
              <td className="px-4 py-3.5">
                <StatusBadge status={emp.status} />
              </td>
              <td className="px-4 py-3.5 text-right">
                <div className="dropdown dropdown-end">
                  <div tabIndex={0} role="button" className="btn btn-xs btn-ghost btn-circle text-slate-500">
                    <MoreVertical className="w-4 h-4" />
                  </div>
                  <ul tabIndex={0} className="dropdown-content z-10 menu p-1 shadow-lg bg-white rounded-xl w-44 border border-slate-100 text-xs">
                    <li>
                      <button onClick={() => onRecordAttendance(emp)} className="flex items-center gap-2 py-2 text-blue-600 font-semibold">
                        <Clock className="w-3.5 h-3.5" /> Log Attendance
                      </button>
                    </li>
                    <li>
                      <button onClick={() => onEdit(emp)} className="flex items-center gap-2 py-2">
                        <Edit3 className="w-3.5 h-3.5 text-amber-600" /> Edit Record
                      </button>
                    </li>
                    <li>
                      <button onClick={() => onDelete(emp.id)} className="flex items-center gap-2 py-2 text-rose-600">
                        <Trash2 className="w-3.5 h-3.5" /> Delete
                      </button>
                    </li>
                  </ul>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default EmployeeTable;
