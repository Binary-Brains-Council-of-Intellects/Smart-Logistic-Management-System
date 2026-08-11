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

const SeasonalTrendChart = ({ data }) => {
  const chartData = {
    labels: data.map((d) => d.season),
    datasets: [
      {
        label: 'Sales Volume',
        data: data.map((d) => d.sales),
        backgroundColor: ['#3b82f6', '#f59e0b', '#d97706', '#0284c7'],
        borderRadius: 8
      }
    ]
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: false },
      tooltip: {
        callbacks: {
          label: (context) => ` Seasonal Volume: ${context.raw} units`
        }
      }
    },
    scales: {
      y: { grid: { color: '#f1f5f9' } },
      x: { grid: { display: false } }
    }
  };

  return (
    <div className="h-60 w-full">
      <Bar data={chartData} options={options} />
    </div>
  );
};

export default SeasonalTrendChart;
