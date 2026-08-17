import React from 'react';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { Doughnut } from 'react-chartjs-2';

ChartJS.register(ArcElement, Tooltip, Legend);

const ExchangeChart = ({ exchanges }) => {
  const counts = {
    WRONG_PRODUCT: 0,
    DAMAGED: 0,
    EXPIRED_ON_ARRIVAL: 0,
    OTHER: 0
  };

  if (Array.isArray(exchanges) && exchanges.length > 0) {
    exchanges.forEach((exc) => {
      const r = (exc.reason || '').toUpperCase();
      if (r.includes('WRONG')) {
        counts.WRONG_PRODUCT += 1;
      } else if (r.includes('DAMAGE') || r.includes('BROKEN')) {
        counts.DAMAGED += 1;
      } else if (r.includes('EXPIR')) {
        counts.EXPIRED_ON_ARRIVAL += 1;
      } else {
        counts.OTHER += 1;
      }
    });
  }

  const hasData = Array.isArray(exchanges) && exchanges.length > 0;

  const chartData = {
    labels: ['Wrong Product', 'Damaged Packaging', 'Expired on Arrival', 'Other'],
    datasets: [
      {
        data: hasData
          ? [counts.WRONG_PRODUCT, counts.DAMAGED, counts.EXPIRED_ON_ARRIVAL, counts.OTHER]
          : [14, 9, 5, 3],
        backgroundColor: ['#f59e0b', '#ef4444', '#dc2626', '#94a3b8'],
        borderWidth: 2,
        borderColor: '#ffffff'
      }
    ]
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          boxWidth: 12,
          font: { size: 11 }
        }
      }
    }
  };

  return (
    <div className="h-60 w-full">
      <Doughnut data={chartData} options={options} />
    </div>
  );
};

export default ExchangeChart;
