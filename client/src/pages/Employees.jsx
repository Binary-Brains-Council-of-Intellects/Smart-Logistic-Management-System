import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import PageHeader from '../components/common/PageHeader';
import EmployeeTable from '../components/employees/EmployeeTable';
import EmployeeFormModal from '../components/employees/EmployeeFormModal';
import AttendanceTable from '../components/employees/AttendanceTable';
import AttendanceFormModal from '../components/employees/AttendanceFormModal';
import PayrollTable from '../components/employees/PayrollTable';
import { Plus, Clock, Users, CalendarCheck, DollarSign } from 'lucide-react';
import { useSLMS } from '../context/SLMSContext';

const Employees = () => {
  const { employees, addEmployee, updateEmployee, deleteEmployee, attendance, addAttendanceRecord } = useSLMS();

  const location = useLocation();
  const navigate = useNavigate();

  // Determine active tab from URL path
  const getActiveTab = () => {
    if (location.pathname.includes('/attendance')) return 'attendance';
    if (location.pathname.includes('/payroll')) return 'payroll';
    return 'directory';
  };

  const activeTab = getActiveTab();

  const handleTabChange = (tab) => {
    if (tab === 'attendance') navigate('/employees/attendance');
    else if (tab === 'payroll') navigate('/employees/payroll');
    else navigate('/employees');
  };

  // Modals
  const [isEmployeeModalOpen, setIsEmployeeModalOpen] = useState(false);
  const [editingEmployee, setEditingEmployee] = useState(null);
  const [isAttendanceModalOpen, setIsAttendanceModalOpen] = useState(false);
  const [preselectedEmpForAttendance, setPreselectedEmpForAttendance] = useState(null);

  const handleOpenAddEmployee = () => {
    setEditingEmployee(null);
    setIsEmployeeModalOpen(true);
  };

  const handleOpenEditEmployee = (emp) => {
    setEditingEmployee(emp);
    setIsEmployeeModalOpen(true);
  };

  const handleOpenRecordAttendance = (emp = null) => {
    setPreselectedEmpForAttendance(emp);
    setIsAttendanceModalOpen(true);
  };

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <PageHeader
        title="Employees & Workforce Payroll"
        subtitle="Manage employee records, daily attendance, and automated payroll calculations."
      >
        <button
          onClick={() => handleOpenRecordAttendance()}
          className="btn btn-sm btn-outline border-slate-300 text-slate-700 hover:bg-slate-100 rounded-xl gap-1.5"
        >
          <Clock className="w-4 h-4 text-blue-600" />
          <span>Record Attendance</span>
        </button>
        <button
          onClick={handleOpenAddEmployee}
          className="btn btn-sm bg-blue-600 hover:bg-blue-700 text-white rounded-xl gap-1.5 border-0 shadow-sm"
        >
          <Plus className="w-4 h-4" />
          <span>Add Employee</span>
        </button>
      </PageHeader>

      {/* Tabs Navigation */}
      <div className="flex border-b border-slate-200 gap-2">
        <button
          onClick={() => handleTabChange('directory')}
          className={`flex items-center gap-2 px-4 py-2.5 text-xs font-bold border-b-2 transition-all ${
            activeTab === 'directory'
              ? 'border-blue-600 text-blue-600'
              : 'border-transparent text-slate-500 hover:text-slate-800'
          }`}
        >
          <Users className="w-4 h-4" />
          <span>Employee Directory ({employees.length})</span>
        </button>

        <button
          onClick={() => handleTabChange('attendance')}
          className={`flex items-center gap-2 px-4 py-2.5 text-xs font-bold border-b-2 transition-all ${
            activeTab === 'attendance'
              ? 'border-blue-600 text-blue-600'
              : 'border-transparent text-slate-500 hover:text-slate-800'
          }`}
        >
          <CalendarCheck className="w-4 h-4" />
          <span>Attendance Logs ({attendance.length})</span>
        </button>

        <button
          onClick={() => handleTabChange('payroll')}
          className={`flex items-center gap-2 px-4 py-2.5 text-xs font-bold border-b-2 transition-all ${
            activeTab === 'payroll'
              ? 'border-blue-600 text-blue-600'
              : 'border-transparent text-slate-500 hover:text-slate-800'
          }`}
        >
          <DollarSign className="w-4 h-4" />
          <span>Monthly Payroll Summary</span>
        </button>
      </div>

      {/* Tab Contents */}
      {activeTab === 'directory' && (
        <EmployeeTable
          employees={employees}
          onEdit={handleOpenEditEmployee}
          onDelete={(id) => {
            if (window.confirm('Delete employee record from system?')) {
              deleteEmployee(id);
            }
          }}
          onRecordAttendance={handleOpenRecordAttendance}
        />
      )}

      {activeTab === 'attendance' && <AttendanceTable attendanceRecords={attendance} />}

      {activeTab === 'payroll' && <PayrollTable employees={employees} attendance={attendance} />}

      {/* Modals */}
      <EmployeeFormModal
        isOpen={isEmployeeModalOpen}
        onClose={() => setIsEmployeeModalOpen(false)}
        onSave={(data) => (editingEmployee ? updateEmployee(data) : addEmployee(data))}
        initialData={editingEmployee}
      />

      <AttendanceFormModal
        isOpen={isAttendanceModalOpen}
        onClose={() => setIsAttendanceModalOpen(false)}
        onSave={(data) => addAttendanceRecord(data)}
        preselectedEmployee={preselectedEmpForAttendance}
      />
    </div>
  );
};

export default Employees;
