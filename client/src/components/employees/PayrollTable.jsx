import React from 'react';

const PayrollTable = ({ employees, attendance }) => {
  const payrollData = employees.map((emp) => {
    const empId = String(emp.id || emp.employeeId);
    const empAttendance = attendance.filter(
      (a) => String(a.employeeId) === empId || (a.employeeName && emp.name && a.employeeName.toLowerCase() === emp.name.toLowerCase())
    );
    const recordedHours = empAttendance.reduce((sum, a) => sum + Number(a.hoursWorked || 0), 0);
    const totalHoursWorked = recordedHours;
    const rate = Number(emp.hourlyRate !== undefined ? emp.hourlyRate : 200);
    const monthlySalary = totalHoursWorked * rate;

    return {
      ...emp,
      id: emp.id || emp.employeeId,
      name: emp.name || 'Employee',
      designation: emp.designation || 'Warehouse Operator',
      recordedHours,
      totalHoursWorked,
      hourlyRate: rate,
      monthlySalary
    };
  });

  const totalPayrollAmount = payrollData.reduce((sum, emp) => sum + emp.monthlySalary, 0);

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between p-5 bg-slate-900 text-white rounded-2xl shadow-sm">
        <div>
          <span className="text-xs text-slate-400 font-bold uppercase tracking-wider block">Total Monthly Payroll Expense</span>
          <p className="text-xs text-slate-300">Automated calculation: Monthly Salary = Total Hours Worked × Hourly Rate</p>
        </div>
        <span className="text-3xl font-extrabold text-emerald-400">৳{totalPayrollAmount.toLocaleString()}</span>
      </div>

      <div className="overflow-x-auto bg-white rounded-2xl border border-slate-200 shadow-2xs">
        <table className="table table-sm sm:table-md w-full">
          <thead>
            <tr className="bg-slate-50 text-slate-600 text-xs uppercase tracking-wider border-b border-slate-200">
              <th className="py-3.5 px-4 font-bold">Employee ID</th>
              <th className="py-3.5 px-4 font-bold">Employee Name</th>
              <th className="py-3.5 px-4 font-bold">Designation</th>
              <th className="py-3.5 px-4 font-bold">Hours Worked</th>
              <th className="py-3.5 px-4 font-bold">Hourly Rate</th>
              <th className="py-3.5 px-4 font-bold text-right">Monthly Salary</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100 text-sm text-slate-800 font-medium">
            {payrollData.map((emp) => (
              <tr key={emp.id} className="hover:bg-slate-50/80 transition-colors">
                <td className="px-4 py-3.5 font-mono text-xs text-slate-500 font-semibold">{emp.id}</td>
                <td className="px-4 py-3.5 font-bold text-slate-900">{emp.name}</td>
                <td className="px-4 py-3.5 text-xs text-slate-600">{emp.designation}</td>
                <td className="px-4 py-3.5 font-semibold text-slate-700">{emp.totalHoursWorked} hrs</td>
                <td className="px-4 py-3.5 text-slate-700">৳{emp.hourlyRate} / hr</td>
                <td className="px-4 py-3.5 text-right font-extrabold text-emerald-700">
                  ৳{emp.monthlySalary.toLocaleString()}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default PayrollTable;
