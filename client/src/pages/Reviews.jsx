import React, { useState } from 'react';
import PageHeader from '../components/common/PageHeader';
import ReviewTable from '../components/reviews/ReviewTable';
import ReviewFormModal from '../components/reviews/ReviewFormModal';
import ExchangeTable from '../components/reviews/ExchangeTable';
import ExchangeFormModal from '../components/reviews/ExchangeFormModal';
import StatCard from '../components/common/StatCard';
import { Star, MessageSquare, RotateCcw, Plus } from 'lucide-react';
import { useSLMS } from '../context/SLMSContext';

const Reviews = () => {
  const { reviews, addReview, exchanges, addExchange } = useSLMS();
  const [activeTab, setActiveTab] = useState('reviews');

  // Modals
  const [isReviewModalOpen, setIsReviewModalOpen] = useState(false);
  const [isExchangeModalOpen, setIsExchangeModalOpen] = useState(false);

  // Reviews aggregated stats
  const totalReviews = reviews.length;
  const avgRating = totalReviews > 0 ? (reviews.reduce((acc, r) => acc + r.rating, 0) / totalReviews).toFixed(1) : '5.0';
  const fiveStarCount = reviews.filter((r) => r.rating === 5).length;
  const oneStarCount = reviews.filter((r) => r.rating === 1).length;

  // Exchanges aggregated stats
  const totalExchanges = exchanges.length;
  const wrongProductCount = exchanges.filter((e) => e.reason === 'WRONG_PRODUCT').length;
  const damagedCount = exchanges.filter((e) => e.reason === 'DAMAGED').length;
  const expiredArrivalCount = exchanges.filter((e) => e.reason === 'EXPIRED_ON_ARRIVAL').length;

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <PageHeader
        title="Customer Feedback & Returns"
        subtitle="Manage product customer ratings, review feedback, and return/exchange logistics."
      >
        {activeTab === 'reviews' ? (
          <button
            onClick={() => setIsReviewModalOpen(true)}
            className="btn btn-sm bg-blue-600 hover:bg-blue-700 text-white rounded-xl gap-1.5 border-0 shadow-sm"
          >
            <Plus className="w-4 h-4" />
            <span>Submit Review</span>
          </button>
        ) : (
          <button
            onClick={() => setIsExchangeModalOpen(true)}
            className="btn btn-sm bg-blue-600 hover:bg-blue-700 text-white rounded-xl gap-1.5 border-0 shadow-sm"
          >
            <Plus className="w-4 h-4" />
            <span>Record Exchange</span>
          </button>
        )}
      </PageHeader>

      {/* Tabs Navigation */}
      <div className="flex border-b border-slate-200 gap-2">
        <button
          onClick={() => setActiveTab('reviews')}
          className={`flex items-center gap-2 px-4 py-2.5 text-xs font-bold border-b-2 transition-all ${
            activeTab === 'reviews'
              ? 'border-blue-600 text-blue-600'
              : 'border-transparent text-slate-500 hover:text-slate-800'
          }`}
        >
          <MessageSquare className="w-4 h-4" />
          <span>Customer Reviews ({totalReviews})</span>
        </button>

        <button
          onClick={() => setActiveTab('exchanges')}
          className={`flex items-center gap-2 px-4 py-2.5 text-xs font-bold border-b-2 transition-all ${
            activeTab === 'exchanges'
              ? 'border-blue-600 text-blue-600'
              : 'border-transparent text-slate-500 hover:text-slate-800'
          }`}
        >
          <RotateCcw className="w-4 h-4" />
          <span>Exchanges &amp; Returns ({totalExchanges})</span>
        </button>
      </div>

      {/* Tab 1: Reviews */}
      {activeTab === 'reviews' && (
        <div className="space-y-6">
          {/* Summary Cards */}
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
            <StatCard title="Total Reviews" value={totalReviews} subtitle="Customer ratings logged" icon={MessageSquare} />
            <StatCard title="Average Rating" value={`${avgRating} ★`} subtitle="Out of 5.0 scale" icon={Star} badgeText="High Satisfaction" badgeColor="badge-success" />
            <StatCard title="5-Star Reviews" value={fiveStarCount} subtitle="Top customer satisfaction" badgeText="Excellent" badgeColor="badge-success" />
            <StatCard title="1-Star Reviews" value={oneStarCount} subtitle="Negative reviews alert" badgeText="Minimal" badgeColor="badge-ghost" />
          </div>

          <ReviewTable reviews={reviews} />
        </div>
      )}

      {/* Tab 2: Exchanges */}
      {activeTab === 'exchanges' && (
        <div className="space-y-6">
          {/* Summary Cards */}
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
            <StatCard title="Total Exchanges" value={totalExchanges} subtitle="Return requests logged" icon={RotateCcw} />
            <StatCard title="Wrong Product" value={wrongProductCount} subtitle="Packaging picking errors" badgeText="Dispatch Error" badgeColor="badge-warning" />
            <StatCard title="Damaged Packaging" value={damagedCount} subtitle="Transit physical damage" badgeText="Handling Alert" badgeColor="badge-error" />
            <StatCard title="Expired on Arrival" value={expiredArrivalCount} subtitle="Shelf-life breach" badgeText="Quality Issue" badgeColor="badge-error" />
          </div>

          <ExchangeTable exchanges={exchanges} />
        </div>
      )}

      {/* Modals */}
      <ReviewFormModal
        isOpen={isReviewModalOpen}
        onClose={() => setIsReviewModalOpen(false)}
        onSave={(data) => addReview(data)}
      />

      <ExchangeFormModal
        isOpen={isExchangeModalOpen}
        onClose={() => setIsExchangeModalOpen(false)}
        onSave={(data) => addExchange(data)}
      />
    </div>
  );
};

export default Reviews;
