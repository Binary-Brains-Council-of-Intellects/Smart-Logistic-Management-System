import React, { useState, useEffect } from 'react';
import Modal from '../common/Modal';
import { useSLMS } from '../../context/SLMSContext';

const AttendanceFormModal = ({ isOpen, onClose, onSave, preselectedEmployee }) => {
  const { employees } = useSLMS();

  const [employeeId, setEmployeeId] = useState('');
  const [date, setDate] = useState(new Date().toISOString().split('T')[0]);
  const [checkIn, setCheckIn] = useState('09:00');
  const [checkOut, setCheckOut] = useState('17:00');

  useEffect(() => {
    if (isOpen) {
      if (preselectedEmployee) {
        setEmployeeId(preselectedEmployee.id);
      } else if (employees.length > 0) {
        setEmployeeId(employees[0].id);
      }
      setDate(new Date().toISOString().split('T')[0]);
      setCheckIn('09:00');
      setCheckOut('17:00');
    }
  }, [isOpen, preselectedEmployee, employees]);

  const calculateHours = (inTime, outTime) => {
    if (!inTime || !outTime) return 8;
    const [inH, inM] = inTime.split(':').map(Number);
    const [outH, outM] = outTime.split(':').map(Number);
    const startMins = inH * 60 + inM;
    const endMins = outH * 60 + outM;
    const diff = (endMins - startMins) / 60;
    return diff > 0 ? Number(diff.toFixed(1)) : 8;
  };

  const computedHours = calculateHours(checkIn, checkOut);

  const handleSubmit = (e) => {
    e.preventDefault();
    const activeEmpId = employeeId || (employees[0]?.id || employees[0]?.employeeId || '');
    const selectedEmp = employees.find(
      (emp) => String(emp.id || emp.employeeId) === String(activeEmpId)
    );

    onSave({
      employeeId: activeEmpId,
      employeeName: selectedEmp ? selectedEmp.name : 'Employee',
      date: date || new Date().toISOString().split('T')[0],
      checkIn: checkIn || '09:00',
      checkOut: checkOut || '17:00',
      hoursWorked: computedHours
    });
    onClose();
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Record Daily Employee Attendance">
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="label text-xs font-bold text-slate-700">Select Employee *</label>
          <select
            value={employeeId}
            onChange={(e) => setEmployeeId(e.target.value)}
            className="select select-sm border-slate-200 w-full text-xs"
          >
            {employees.map((emp) => (
              <option key={emp.id} value={emp.id}>
                {emp.name} ({emp.designation})
              </option>
            ))}
          </select>
        </div>

        <div>
          <label className="label text-xs font-bold text-slate-700">Attendance Date *</label>
          <input
            type="date"
            value={date}
            onChange={(e) => setDate(e.target.value)}
            className="input input-sm border-slate-200 w-full text-xs"
          />
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="label text-xs font-bold text-slate-700">Check-In Time *</label>
            <input
              type="time"
              value={checkIn}
              onChange={(e) => setCheckIn(e.target.value)}
              className="input input-sm border-slate-200 w-full text-xs"
            />
          </div>

          <div>
            <label className="label text-xs font-bold text-slate-700">Check-Out Time *</label>
            <input
              type="time"
              value={checkOut}
              onChange={(e) => setCheckOut(e.target.value)}
              className="input input-sm border-slate-200 w-full text-xs"
            />
          </div>
        </div>

        <div className="p-3 bg-blue-50 border border-blue-200 rounded-xl flex items-center justify-between">
          <span className="text-xs font-bold text-blue-900">Calculated Hours Worked</span>
          <span className="text-lg font-extrabold text-blue-600">{computedHours} Hours</span>
        </div>

        <div className="flex items-center justify-end gap-2 pt-3 border-t border-slate-200">
          <button type="button" onClick={onClose} className="btn btn-sm btn-ghost text-slate-600 rounded-xl">
            Cancel
          </button>
          <button type="submit" className="btn btn-sm bg-blue-600 hover:bg-blue-700 text-white rounded-xl px-5 border-0">
            Log Attendance
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default AttendanceFormModal;
