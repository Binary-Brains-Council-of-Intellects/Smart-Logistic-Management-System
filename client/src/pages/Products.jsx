import React, { useState } from 'react';
import PageHeader from '../components/common/PageHeader';
import ProductTable from '../components/products/ProductTable';
import ProductFormModal from '../components/products/ProductFormModal';
import ProductDetailsModal from '../components/products/ProductDetailsModal';
import EmptyState from '../components/common/EmptyState';
import { Plus, Search, Filter, RefreshCw } from 'lucide-react';
import { useSLMS } from '../context/SLMSContext';

const Products = () => {
  const { products, addProduct, updateProduct, deleteProduct } = useSLMS();

  const [searchTerm, setSearchTerm] = useState('');
  const [categoryFilter, setCategoryFilter] = useState('');
  const [stockFilter, setStockFilter] = useState('');
  const [expiryFilter, setExpiryFilter] = useState('');

  // Modal controls
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);
  const [selectedProductDetails, setSelectedProductDetails] = useState(null);

  const handleOpenAdd = () => {
    setEditingProduct(null);
    setIsFormOpen(true);
  };

  const handleOpenEdit = (p) => {
    setEditingProduct(p);
    setIsFormOpen(true);
  };

  const handleSaveProduct = (formData) => {
    if (editingProduct) {
      updateProduct(formData);
    } else {
      addProduct(formData);
    }
  };

  const handleResetFilters = () => {
    setSearchTerm('');
    setCategoryFilter('');
    setStockFilter('');
    setExpiryFilter('');
  };

  // Filter logic
  const filteredProducts = products.filter((p) => {
    // Search matching
    const matchesSearch =
      p.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      p.id.toLowerCase().includes(searchTerm.toLowerCase()) ||
      p.batchNumber.toLowerCase().includes(searchTerm.toLowerCase());

    // Category matching
    const matchesCategory = categoryFilter ? p.category === categoryFilter : true;

    // Stock status matching
    let matchesStock = true;
    if (stockFilter === 'IN_STOCK') matchesStock = Number(p.availableQuantity) > 10;
    if (stockFilter === 'LOW_STOCK') matchesStock = Number(p.availableQuantity) > 0 && Number(p.availableQuantity) <= 10;
    if (stockFilter === 'OUT_OF_STOCK') matchesStock = Number(p.availableQuantity) === 0;

    // Expiry status matching
    let matchesExpiry = true;
    if (expiryFilter) {
      if (p.type !== 'PerishableProduct' || !p.expiryDate) {
        matchesExpiry = false;
      } else {
        const today = new Date();
        const expiry = new Date(p.expiryDate);
        const diffDays = Math.ceil((expiry - today) / (1000 * 60 * 60 * 24));
        if (expiryFilter === 'VALID') matchesExpiry = diffDays > 30;
        if (expiryFilter === 'NEAR_EXPIRY') matchesExpiry = diffDays >= 0 && diffDays <= 30;
        if (expiryFilter === 'EXPIRED') matchesExpiry = diffDays < 0;
      }
    }

    return matchesSearch && matchesCategory && matchesStock && matchesExpiry;
  });

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <PageHeader
        title="Product Catalog"
        subtitle="Manage inventory, stock levels, batches, and expiry information."
      >
        <button
          onClick={handleOpenAdd}
          className="btn btn-sm bg-blue-600 hover:bg-blue-700 text-white rounded-xl gap-1.5 border-0 shadow-sm"
        >
          <Plus className="w-4 h-4" />
          <span>Add Product</span>
        </button>
      </PageHeader>

      {/* Search & Filters Controls Bar */}
      <div className="bg-white rounded-2xl border border-slate-200 p-4 shadow-2xs space-y-3">
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-3">
          {/* Search Box */}
          <div className="relative">
            <input
              type="text"
              placeholder="Search product name, ID, batch..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="input input-sm border-slate-200 w-full pl-9 text-xs rounded-xl focus:ring-2 focus:ring-blue-500/20"
            />
            <Search className="w-4 h-4 text-slate-400 absolute left-3 top-2.5" />
          </div>

          {/* Category Filter */}
          <div>
            <select
              value={categoryFilter}
              onChange={(e) => setCategoryFilter(e.target.value)}
              className="select select-sm border-slate-200 w-full text-xs rounded-xl"
            >
              <option value="">All Categories</option>
              <option value="ELECTRONICS">Electronics</option>
              <option value="GROCERY">Grocery</option>
              <option value="PHARMACEUTICAL">Pharmaceutical</option>
              <option value="CLOTHING">Clothing</option>
              <option value="OTHER">Other</option>
            </select>
          </div>

          {/* Stock Status Filter */}
          <div>
            <select
              value={stockFilter}
              onChange={(e) => setStockFilter(e.target.value)}
              className="select select-sm border-slate-200 w-full text-xs rounded-xl"
            >
              <option value="">All Stock Statuses</option>
              <option value="IN_STOCK">In Stock (&gt;10 units)</option>
              <option value="LOW_STOCK">Low Stock (1-10 units)</option>
              <option value="OUT_OF_STOCK">Out of Stock (0 units)</option>
            </select>
          </div>

          {/* Expiry Status Filter */}
          <div>
            <select
              value={expiryFilter}
              onChange={(e) => setExpiryFilter(e.target.value)}
              className="select select-sm border-slate-200 w-full text-xs rounded-xl"
            >
              <option value="">All Expiry Statuses</option>
              <option value="VALID">Valid Expiry</option>
              <option value="NEAR_EXPIRY">Near Expiry (&le;30 days)</option>
              <option value="EXPIRED">Expired</option>
            </select>
          </div>
        </div>

        {(searchTerm || categoryFilter || stockFilter || expiryFilter) && (
          <div className="flex items-center justify-between pt-2 border-t border-slate-100 text-xs">
            <span className="text-slate-500 font-medium">
              Showing {filteredProducts.length} of {products.length} products
            </span>
            <button
              onClick={handleResetFilters}
              className="text-blue-600 hover:underline font-bold flex items-center gap-1"
            >
              <RefreshCw className="w-3 h-3" /> Reset Filters
            </button>
          </div>
        )}
      </div>

      {/* Main Table or Empty State */}
      {filteredProducts.length === 0 ? (
        <EmptyState
          title="No products match your criteria"
          message="Try adjusting search terms, categories, or status filters."
          onReset={handleResetFilters}
        />
      ) : (
        <ProductTable
          products={filteredProducts}
          onViewDetails={(p) => setSelectedProductDetails(p)}
          onEdit={handleOpenEdit}
          onDelete={(id) => {
            if (window.confirm('Are you sure you want to delete this product catalog item?')) {
              deleteProduct(id);
            }
          }}
        />
      )}

      {/* Modals */}
      <ProductFormModal
        isOpen={isFormOpen}
        onClose={() => setIsFormOpen(false)}
        onSave={handleSaveProduct}
        initialData={editingProduct}
      />

      <ProductDetailsModal
        isOpen={!!selectedProductDetails}
        onClose={() => setSelectedProductDetails(null)}
        product={selectedProductDetails}
      />
    </div>
  );
};

export default Products;
