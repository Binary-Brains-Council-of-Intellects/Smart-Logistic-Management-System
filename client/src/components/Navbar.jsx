import { useState } from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import {
    Truck,
    Search,
    Package,
    Phone,
    Mail,
    Globe,
    Menu,
    X,
    User,
    ArrowRight,
    ChevronDown,
    Navigation,
    ShieldCheck,
    Zap,
    BarChart3
} from 'lucide-react';

const Navbar = () => {
    const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
    const [isServicesOpen, setIsServicesOpen] = useState(false);
    const [trackingId, setTrackingId] = useState('');
    const [showQuickTrack, setShowQuickTrack] = useState(false);
    const navigate = useNavigate();

    const handleTrackSubmit = (e) => {
        e.preventDefault();
        if (trackingId.trim()) {
            navigate(`/track?id=${encodeURIComponent(trackingId.trim())}`);
            setShowQuickTrack(false);
            setTrackingId('');
        }
    };

    const navLinks = [
        { name: 'Home', path: '/' },
        { name: 'Track Package', path: '/track' },
        { name: 'Fleet Telematics', path: '/fleet' },
        { name: 'Rate Estimator', path: '/calculator' },
        { name: 'About Us', path: '/about' },
        { name: 'Contact', path: '/contact' },
    ];

    const serviceItems = [
        {
            title: 'Freight & Dispatch',
            desc: 'FTL & LTL autonomous dispatch network',
            icon: Truck,
            path: '/services/freight'
        },
        {
            title: 'Smart Warehousing',
            desc: 'Robotic inventory & automated fulfillment',
            icon: Package,
            path: '/services/warehousing'
        },
        {
            title: 'AI Route Optimizer',
            desc: 'Dynamic traffic & fuel-efficient routing',
            icon: Navigation,
            path: '/services/route-optimizer'
        },
        {
            title: 'Analytics & Telematics',
            desc: 'Real-time IoT sensor telemetry data',
            icon: BarChart3,
            path: '/services/analytics'
        },
    ];

    return (
        <header className="w-full sticky top-0 z-50 transition-all duration-300">
            {/* Main Navbar */}
            <nav className="bg-white/95 backdrop-blur-md border-b border-slate-200/80 shadow-xs">
                <div className=" mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex items-center justify-between h-20">

                        {/* Brand Logo */}
                        <Link to="/" className="flex items-center gap-3 group">
                            <div className="relative flex items-center justify-center w-11 h-11 rounded-xl bg-gradient-to-tr from-blue-600 via-indigo-600 to-cyan-500 text-white shadow-md shadow-blue-500/20 group-hover:scale-105 transition-transform duration-300">
                                <Truck className="w-6 h-6 stroke-[2.2]" />
                                <Zap className="w-3 h-3 text-amber-300 absolute -top-1 -right-1 fill-amber-300 animate-pulse" />
                            </div>
                            <div className="flex flex-col">
                                <div className="flex items-center gap-1.5">
                                    <span className="text-xl font-black tracking-tight text-slate-900">
                                        Smart<span className="text-blue-600">Logistics</span>
                                    </span>
                                </div>
                                <span className="text-[10px] text-slate-500 font-medium tracking-wider uppercase">
                                    Autonomous Fleet & Freight
                                </span>
                            </div>
                        </Link>

                        {/* Desktop Navigation Links */}
                        <div className="hidden lg:flex items-center gap-1">
                            {/* Regular Links */}
                            <NavLink
                                to="/"
                                className={({ isActive }) =>
                                    `px-3.5 py-2 rounded-lg text-sm font-semibold transition-colors ${isActive
                                        ? 'text-blue-600 bg-blue-50/80'
                                        : 'text-slate-700 hover:text-blue-600 hover:bg-slate-50'
                                    }`
                                }
                            >
                                Home
                            </NavLink>

                            {/* Services Dropdown */}
                            <div
                                className="relative"
                                onMouseEnter={() => setIsServicesOpen(true)}
                                onMouseLeave={() => setIsServicesOpen(false)}
                            >
                                <button
                                    className="flex items-center gap-1 px-3.5 py-2 rounded-lg text-sm font-semibold text-slate-700 hover:text-blue-600 hover:bg-slate-50 transition-colors"
                                    aria-expanded={isServicesOpen}
                                >
                                    <span>Services</span>
                                    <ChevronDown className={`w-4 h-4 transition-transform duration-200 ${isServicesOpen ? 'rotate-180 text-blue-600' : 'text-slate-400'}`} />
                                </button>

                                {/* Dropdown Menu */}
                                {isServicesOpen && (
                                    <div className="absolute top-full left-0 w-80 pt-2 z-50">
                                        <div className="bg-white rounded-2xl shadow-xl border border-slate-100 p-2 grid gap-1 backdrop-blur-xl">
                                            {serviceItems.map((item, idx) => {
                                                const IconComponent = item.icon;
                                                return (
                                                    <Link
                                                        key={idx}
                                                        to={item.path}
                                                        className="flex items-start gap-3 p-3 rounded-xl hover:bg-slate-50 transition-all group/item"
                                                        onClick={() => setIsServicesOpen(false)}
                                                    >
                                                        <div className="p-2.5 rounded-lg bg-blue-50 text-blue-600 group-hover/item:bg-blue-600 group-hover/item:text-white transition-colors">
                                                            <IconComponent className="w-5 h-5" />
                                                        </div>
                                                        <div>
                                                            <div className="text-sm font-bold text-slate-900 group-hover/item:text-blue-600 transition-colors">
                                                                {item.title}
                                                            </div>
                                                            <div className="text-xs text-slate-500 leading-snug">
                                                                {item.desc}
                                                            </div>
                                                        </div>
                                                    </Link>
                                                );
                                            })}
                                        </div>
                                    </div>
                                )}
                            </div>

                            {navLinks.slice(1).map((link) => (
                                <NavLink
                                    key={link.path}
                                    to={link.path}
                                    className={({ isActive }) =>
                                        `px-3.5 py-2 rounded-lg text-sm font-semibold transition-colors ${isActive
                                            ? 'text-blue-600 bg-blue-50/80'
                                            : 'text-slate-700 hover:text-blue-600 hover:bg-slate-50'
                                        }`
                                    }
                                >
                                    {link.name}
                                </NavLink>
                            ))}
                        </div>

                        {/* Right Action Utilities */}
                        <div className="hidden md:flex items-center gap-3">
                            {/* Quick Search Toggle / Input */}
                            <div className="relative">
                                <button
                                    onClick={() => setShowQuickTrack(!showQuickTrack)}
                                    className="flex items-center gap-2 px-3 py-2 text-xs font-semibold text-slate-600 bg-slate-100 hover:bg-slate-200/80 rounded-xl transition-colors border border-slate-200"
                                    title="Quick Track Shipment"
                                >
                                    <Search className="w-4 h-4 text-blue-600" />
                                    <span>Quick Track</span>
                                </button>

                                {/* Popover Track Search */}
                                {showQuickTrack && (
                                    <div className="absolute right-0 top-full mt-2 w-80 bg-white rounded-2xl shadow-2xl border border-slate-100 p-4 z-50">
                                        <div className="flex justify-between items-center mb-2">
                                            <span className="text-xs font-bold text-slate-800 uppercase tracking-wider flex items-center gap-1.5">
                                                <Package className="w-4 h-4 text-blue-600" /> Track Shipment
                                            </span>
                                            <button
                                                onClick={() => setShowQuickTrack(false)}
                                                className="text-slate-400 hover:text-slate-600 p-1"
                                            >
                                                <X className="w-4 h-4" />
                                            </button>
                                        </div>
                                        <form onSubmit={handleTrackSubmit} className="space-y-2">
                                            <div className="relative">
                                                <input
                                                    type="text"
                                                    placeholder="Enter Waybill / Tracking ID..."
                                                    value={trackingId}
                                                    onChange={(e) => setTrackingId(e.target.value)}
                                                    className="w-full px-3.5 py-2.5 text-sm bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 text-slate-800 placeholder-slate-400"
                                                    autoFocus
                                                />
                                            </div>
                                            <button
                                                type="submit"
                                                className="w-full py-2.5 bg-blue-600 hover:bg-blue-700 text-white text-xs font-bold rounded-xl shadow-md shadow-blue-500/20 transition-all flex items-center justify-center gap-1.5"
                                            >
                                                <span>Search Status</span>
                                                <ArrowRight className="w-4 h-4" />
                                            </button>
                                        </form>
                                    </div>
                                )}
                            </div>

                            {/* Login Button */}
                            <Link
                                to="/login"
                                className="flex items-center gap-1.5 px-4 py-2 text-sm font-bold text-slate-700 hover:text-blue-600 transition-colors"
                            >
                                <User className="w-4 h-4" />
                                <span>Portal Login</span>
                            </Link>

                            {/* Get Quote CTA */}
                            <Link
                                to="/quote"
                                className="flex items-center gap-2 px-5 py-2.5 bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white text-sm font-bold rounded-xl shadow-md shadow-blue-600/25 hover:shadow-lg hover:shadow-blue-600/35 hover:-translate-y-0.5 transition-all"
                            >
                                <span>Request Quote</span>
                                <ArrowRight className="w-4 h-4" />
                            </Link>
                        </div>

                        {/* Mobile Hamburger Button */}
                        <div className="flex md:hidden items-center gap-2">
                            <button
                                onClick={() => setShowQuickTrack(!showQuickTrack)}
                                className="p-2 text-slate-600 bg-slate-100 rounded-lg hover:bg-slate-200"
                                aria-label="Toggle Quick Track"
                            >
                                <Search className="w-5 h-5 text-blue-600" />
                            </button>
                            <button
                                onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
                                className="p-2 text-slate-700 hover:text-blue-600 hover:bg-slate-100 rounded-xl transition-colors"
                                aria-label="Toggle Navigation Menu"
                            >
                                {isMobileMenuOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
                            </button>
                        </div>
                    </div>
                </div>

                {/* Mobile Navigation Drawer */}
                {isMobileMenuOpen && (
                    <div className="lg:hidden border-t border-slate-200/80 bg-white/95 backdrop-blur-xl px-4 pt-3 pb-6 space-y-4">
                        {/* Quick Track Input inside mobile drawer */}
                        <form onSubmit={handleTrackSubmit} className="pt-2">
                            <div className="relative">
                                <input
                                    type="text"
                                    placeholder="Enter Waybill / Tracking ID..."
                                    value={trackingId}
                                    onChange={(e) => setTrackingId(e.target.value)}
                                    className="w-full pl-10 pr-24 py-2.5 text-sm bg-slate-100 border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500/20"
                                />
                                <Search className="w-4 h-4 text-slate-400 absolute left-3.5 top-3.5" />
                                <button
                                    type="submit"
                                    className="absolute right-1.5 top-1.5 bottom-1.5 px-3 bg-blue-600 text-white text-xs font-bold rounded-lg hover:bg-blue-700 transition-colors"
                                >
                                    Track
                                </button>
                            </div>
                        </form>

                        <div className="space-y-1">
                            <NavLink
                                to="/"
                                onClick={() => setIsMobileMenuOpen(false)}
                                className={({ isActive }) =>
                                    `block px-4 py-2.5 rounded-xl text-base font-semibold ${isActive ? 'bg-blue-50 text-blue-600 font-bold' : 'text-slate-800 hover:bg-slate-50'
                                    }`
                                }
                            >
                                Home
                            </NavLink>

                            {/* Services Sublinks */}
                            <div className="py-1">
                                <div className="px-4 py-2 text-xs font-bold text-slate-400 uppercase tracking-wider">
                                    Services
                                </div>
                                <div className="pl-4 space-y-1">
                                    {serviceItems.map((item, i) => {
                                        const ItemIcon = item.icon;
                                        return (
                                            <Link
                                                key={i}
                                                to={item.path}
                                                onClick={() => setIsMobileMenuOpen(false)}
                                                className="flex items-center gap-3 px-4 py-2 text-sm font-medium text-slate-700 hover:text-blue-600 hover:bg-slate-50 rounded-lg"
                                            >
                                                <ItemIcon className="w-4 h-4 text-blue-600" />
                                                <span>{item.title}</span>
                                            </Link>
                                        );
                                    })}
                                </div>
                            </div>

                            {navLinks.slice(1).map((link) => (
                                <NavLink
                                    key={link.path}
                                    to={link.path}
                                    onClick={() => setIsMobileMenuOpen(false)}
                                    className={({ isActive }) =>
                                        `block px-4 py-2.5 rounded-xl text-base font-semibold ${isActive ? 'bg-blue-50 text-blue-600 font-bold' : 'text-slate-800 hover:bg-slate-50'
                                        }`
                                    }
                                >
                                    {link.name}
                                </NavLink>
                            ))}
                        </div>

                        {/* Mobile CTAs */}
                        <div className="pt-4 border-t border-slate-200 flex flex-col gap-2">
                            <Link
                                to="/login"
                                onClick={() => setIsMobileMenuOpen(false)}
                                className="w-full py-2.5 text-center text-sm font-bold text-slate-700 bg-slate-100 hover:bg-slate-200 rounded-xl"
                            >
                                Client Portal Login
                            </Link>
                            <Link
                                to="/quote"
                                onClick={() => setIsMobileMenuOpen(false)}
                                className="w-full py-3 text-center text-sm font-bold text-white bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 rounded-xl shadow-md"
                            >
                                Request Freight Quote
                            </Link>
                        </div>
                    </div>
                )}
            </nav>
        </header>
    );
};

export default Navbar;
