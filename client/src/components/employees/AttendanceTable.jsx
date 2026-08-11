import React from 'react';

const AttendanceTable = ({ attendanceRecords }) => {
  return (
    <div className="overflow-x-auto bg-white rounded-2xl border border-slate-200 shadow-2xs">
      <table className="table table-sm sm:table-md w-full">
        <thead>
          <tr className="bg-slate-50 text-slate-600 text-xs uppercase tracking-wider border-b border-slate-200">
            <th className="py-3.5 px-4 font-bold">Record ID</th>
            <th className="py-3.5 px-4 font-bold">Employee</th>
            <th className="py-3.5 px-4 font-bold">Date</th>
            <th className="py-3.5 px-4 font-bold">Check-In</th>
            <th className="py-3.5 px-4 font-bold">Check-Out</th>
            <th className="py-3.5 px-4 font-bold">Hours Worked</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-slate-100 text-sm text-slate-800 font-medium">
          {attendanceRecords.map((att) => (
            <tr key={att.id} className="hover:bg-slate-50/80 transition-colors">
              <td className="px-4 py-3.5 font-mono text-xs text-slate-500 font-semibold">{att.id}</td>
              <td className="px-4 py-3.5 font-bold text-slate-900">{att.employeeName}</td>
              <td className="px-4 py-3.5 text-xs text-slate-600">{att.date}</td>
              <td className="px-4 py-3.5 text-xs font-semibold text-emerald-700 bg-emerald-50 px-2 py-0.5 rounded w-fit">
                {att.checkIn}
              </td>
              <td className="px-4 py-3.5 text-xs font-semibold text-rose-700 bg-rose-50 px-2 py-0.5 rounded w-fit">
                {att.checkOut}
              </td>
              <td className="px-4 py-3.5 font-bold text-blue-600">{att.hoursWorked} hrs</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AttendanceTable;
