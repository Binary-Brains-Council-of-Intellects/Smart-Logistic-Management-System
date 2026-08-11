import React from 'react';
import { useLocation } from 'react-router-dom';
import { Menu, Search, Bell, UserCheck } from 'lucide-react';
import { useSLMS } from '../../context/SLMSContext';

const Navbar = ({ onOpenMobileMenu }) => {
  const location = useLocation();
  const { user } = useSLMS();

  const getPageTitle = (pathname) => {
    if (pathname.includes('/products')) return 'Product Catalog';
    if (pathname.includes('/orders')) return 'Orders Management';
    if (pathname.includes('/employees')) return 'Employees & Payroll';
    if (pathname.includes('/reviews')) return 'Reviews & Exchanges';
    if (pathname.includes('/reports')) return 'Reports & Analytics';
    return 'Dashboard';
  };

  const title = getPageTitle(location.pathname);

  return (
    <header className="sticky top-0 z-30 bg-white border-b border-slate-200 px-4 sm:px-6 py-3.5 flex items-center justify-between shadow-2xs">
      <div className="flex items-center gap-3">
        <button
          onClick={onOpenMobileMenu}
          className="lg:hidden p-2 rounded-xl text-slate-600 hover:bg-slate-100 transition-colors"
          aria-label="Open Mobile Menu"
        >
          <Menu className="w-5 h-5" />
        </button>

        <div>
          <h2 className="text-lg sm:text-xl font-bold text-slate-900 leading-tight">{title}</h2>
          <p className="text-xs text-slate-500 hidden sm:block">Smart Logistics Management System</p>
        </div>
      </div>

      <div className="flex items-center gap-3 sm:gap-4">
        <div className="relative hidden md:block">
          <input
            type="text"
            placeholder="Search warehouse logs..."
            className="w-56 lg:w-64 pl-9 pr-4 py-2 text-xs bg-slate-100 border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 text-slate-800"
          />
          <Search className="w-4 h-4 text-slate-400 absolute left-3 top-2.5" />
        </div>

        <button
          onClick={() => alert('System Notifications: 2 near-expiry batch alerts pending.')}
          className="relative p-2 text-slate-600 hover:bg-slate-100 rounded-xl transition-colors"
          title="Notifications"
        >
          <Bell className="w-5 h-5" />
          <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-amber-500 rounded-full ring-2 ring-white" />
        </button>

        <div className="h-6 w-[1px] bg-slate-200" />

        <div className="flex items-center gap-2.5">
          <div className="w-8 h-8 rounded-full bg-blue-100 text-blue-700 flex items-center justify-center font-bold text-xs">
            <UserCheck className="w-4 h-4" />
          </div>
          <div className="hidden sm:flex flex-col">
            <span className="text-xs font-bold text-slate-900 leading-tight">{user ? user.username : 'Admin User'}</span>
            <span className="text-[10px] text-slate-500 font-medium">Administrator</span>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Navbar;
