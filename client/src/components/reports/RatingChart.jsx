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

const RatingChart = ({ reviews = [] }) => {
  const counts = { 5: 0, 4: 0, 3: 0, 2: 0, 1: 0 };

  if (Array.isArray(reviews) && reviews.length > 0) {
    reviews.forEach((r) => {
      const rating = Math.round(Number(r.rating || 5));
      if (counts[rating] !== undefined) {
        counts[rating] += 1;
      }
    });
  }

  const chartData = {
    labels: ['5 Stars', '4 Stars', '3 Stars', '2 Stars', '1 Star'],
    datasets: [
      {
        label: 'Review Count',
        data: [counts[5], counts[4], counts[3], counts[2], counts[1]],
        backgroundColor: ['#f59e0b', '#fbbf24', '#fcd34d', '#cbd5e1', '#f87171'],
        borderRadius: 6
      }
    ]
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: false }
    },
    scales: {
      y: { grid: { color: '#f1f5f9' }, ticks: { stepSize: 1 } },
      x: { grid: { display: false } }
    }
  };

  return (
    <div className="h-60 w-full">
      <Bar data={chartData} options={options} />
    </div>
  );
};

export default RatingChart;
