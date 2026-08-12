import React from 'react';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Tooltip,
  Legend
} from 'chart.js';
import { Bar } from 'react-chartjs-2';

ChartJS.register(CategoryScale, LinearScale, BarElement, Tooltip, Legend);

const ProductPopularityChart = ({ data = [] }) => {
  const safeData = Array.isArray(data) ? data : [];
  const chartData = {
    labels: safeData.map((item) => item.name || ''),
    datasets: [
      {
        label: 'Units Sold',
        data: safeData.map((item) => item.sold || 0),
        backgroundColor: '#059669',
        borderRadius: 6
      }
    ]
  };

  const options = {
    indexAxis: 'y',
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: false },
      tooltip: {
        callbacks: {
          label: (context) => ` Quantity Sold: ${context.raw} units`
        }
      }
    },
    scales: {
      x: {
        grid: { color: '#f1f5f9' }
      },
      y: {
        grid: { display: false }
      }
    }
  };

  return (
    <div className="h-64 w-full">
      <Bar data={chartData} options={options} />
    </div>
  );
};

export default ProductPopularityChart;
