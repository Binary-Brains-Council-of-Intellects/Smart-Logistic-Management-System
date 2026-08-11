import { createBrowserRouter, Navigate } from "react-router-dom";
import AppLayout from "../components/layout/AppLayout";
import Login from "../pages/Login";
import Dashboard from "../pages/Dashboard";
import Products from "../pages/Products";
import Orders from "../pages/Orders";
import Employees from "../pages/Employees";
import Reviews from "../pages/Reviews";
import Reports from "../pages/Reports";

const Router = createBrowserRouter([
  {
    path: "/login",
    element: <Login />
  },
  {
    path: "/",
    element: <AppLayout />,
    children: [
      {
        path: "/",
        element: <Navigate to="/dashboard" replace />
      },
      {
        path: "/dashboard",
        element: <Dashboard />
      },
      {
        path: "/products",
        element: <Products />
      },
      {
        path: "/orders",
        element: <Orders />
      },
      {
        path: "/employees",
        element: <Employees />
      },
      {
        path: "/employees/attendance",
        element: <Employees />
      },
      {
        path: "/employees/payroll",
        element: <Employees />
      },
      {
        path: "/reviews",
        element: <Reviews />
      },
      {
        path: "/reports",
        element: <Reports />
      }
    ]
  },
  {
    path: "*",
    element: <Navigate to="/dashboard" replace />
  }
]);

export default Router;