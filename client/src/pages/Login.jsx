import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Warehouse, Lock, User, ArrowRight } from 'lucide-react';
import { useSLMS } from '../context/SLMSContext';

const Login = () => {
  const [username, setUsername] = useState('admin');
  const [password, setPassword] = useState('password123');
  const [rememberMe, setRememberMe] = useState(true);
  const [error, setError] = useState('');

  const { login } = useSLMS();
  const navigate = useNavigate();

  const handleLogin = (e) => {
    e.preventDefault();
    if (!username.trim() || !password.trim()) {
      setError('Please enter both username and password.');
      return;
    }

    login(username, password);
    navigate('/dashboard');
  };

  return (
    <div className="min-h-screen bg-slate-900 flex items-center justify-center p-4 relative overflow-hidden">
      {/* Background Subtle Gradient Blobs */}
      <div className="absolute top-10 left-10 w-96 h-96 bg-blue-600/10 rounded-full blur-3xl" />
      <div className="absolute bottom-10 right-10 w-96 h-96 bg-indigo-600/10 rounded-full blur-3xl" />

      <div className="w-full max-w-md bg-white rounded-3xl shadow-2xl border border-slate-100 overflow-hidden relative z-10 p-8 space-y-6">
        {/* Brand Header */}
        <div className="text-center space-y-2">
          <div className="inline-flex items-center justify-center w-14 h-14 rounded-2xl bg-blue-600 text-white shadow-lg shadow-blue-500/30 mb-2">
            <Warehouse className="w-8 h-8 stroke-[2]" />
          </div>
          <h1 className="text-2xl font-extrabold tracking-tight text-slate-900">
            Smart Logistics Management System
          </h1>
          <p className="text-xs font-semibold text-slate-500">Warehouse operations made simple.</p>
        </div>

        {/* Error Alert */}
        {error && (
          <div className="alert alert-error text-xs font-semibold rounded-xl py-2.5">
            <span>{error}</span>
          </div>
        )}

        {/* Login Form */}
        <form onSubmit={handleLogin} className="space-y-4">
          <div>
            <label className="label text-xs font-bold text-slate-700">Username</label>
            <div className="relative">
              <input
                type="text"
                placeholder="Enter username..."
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                className="input input-bordered w-full pl-9 text-xs border-slate-200 rounded-xl focus:border-blue-600"
              />
              <User className="w-4 h-4 text-slate-400 absolute left-3 top-3.5" />
            </div>
          </div>

          <div>
            <label className="label text-xs font-bold text-slate-700">Password</label>
            <div className="relative">
              <input
                type="password"
                placeholder="Enter password..."
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="input input-bordered w-full pl-9 text-xs border-slate-200 rounded-xl focus:border-blue-600"
              />
              <Lock className="w-4 h-4 text-slate-400 absolute left-3 top-3.5" />
            </div>
          </div>

          {/* Remember Me */}
          <div className="flex items-center justify-between text-xs pt-1">
            <label className="flex items-center gap-2 cursor-pointer">
              <input
                type="checkbox"
                checked={rememberMe}
                onChange={(e) => setRememberMe(e.target.checked)}
                className="checkbox checkbox-xs checkbox-primary rounded"
              />
              <span className="text-slate-600 font-medium">Remember credentials</span>
            </label>
            <button
              type="button"
              onClick={() => alert('Demo account credentials: admin / password123')}
              className="text-blue-600 hover:underline font-semibold"
            >
              Demo credentials?
            </button>
          </div>

          {/* Submit Button */}
          <button
            type="submit"
            className="w-full py-3 bg-blue-600 hover:bg-blue-700 text-white font-bold text-sm rounded-xl shadow-md shadow-blue-600/30 transition-all flex items-center justify-center gap-2 mt-4"
          >
            <span>Login to Portal</span>
            <ArrowRight className="w-4 h-4" />
          </button>
        </form>

        {/* Academic Project Footer Notice */}
        <div className="pt-4 border-t border-slate-100 text-center">
          <p className="text-[11px] text-slate-400 font-medium">
            SLMS • Object-Oriented Programming Course Project
          </p>
        </div>
      </div>
    </div>
  );
};

export default Login;
