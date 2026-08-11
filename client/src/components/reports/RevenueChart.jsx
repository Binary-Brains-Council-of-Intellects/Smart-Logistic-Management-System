import React from 'react';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
} from 'chart.js';
import { Bar } from 'react-chartjs-2';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const RevenueChart = ({ data }) => {
  const chartData = {
    labels: data.map((item) => item.month),
    datasets: [
      {
        label: 'Monthly Revenue (৳)',
        data: data.map((item) => item.revenue),
        backgroundColor: '#2563eb',
        borderRadius: 8,
        hoverBackgroundColor: '#1d4ed8'
      }
    ]
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: false
      },
      tooltip: {
        callbacks: {
          label: (context) => ` Revenue: ৳${context.raw.toLocaleString()}`
        }
      }
    },
    scales: {
      y: {
        grid: {
          color: '#f1f5f9'
        },
        ticks: {
          callback: (value) => `৳${value / 1000}K`
        }
      },
      x: {
        grid: {
          display: false
        }
      }
    }
  };

  return (
    <div className="h-64 sm:h-72 w-full">
      <Bar data={chartData} options={options} />
    </div>
  );
};

export default RevenueChart;
