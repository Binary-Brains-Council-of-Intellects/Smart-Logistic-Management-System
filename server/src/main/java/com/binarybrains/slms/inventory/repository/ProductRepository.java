package com.binarybrains.slms.inventory.repository;

import com.binarybrains.slms.inventory.model.Product;
import com.binarybrains.slms.inventory.model.ProductCategory;
import com.binarybrains.slms.inventory.model.ProductType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository for Product documents.
 *
 * Spring Data MongoDB generates the implementation at runtime.
 * The "products" collection stores both PerishableProduct and
 * NonPerishableProduct documents, using the "_class" discriminator
 * to map back to the correct Java subclass.
 */
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findBySku(String sku);

    List<Product> findByCategory(ProductCategory category);

    List<Product> findByProductType(ProductType productType);

    List<Product> findByActiveTrue();

    List<Product> findByActiveFalse();

    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Product> searchByName(String keyword);

    @Query("{ 'expiry_date': { $lte: ?0 }, 'active': true }")
    List<Product> findExpiredProducts(LocalDate currentDate);

    @Query("{ 'expiry_date': { $gt: ?0, $lte: ?1 }, 'active': true }")
    List<Product> findNearExpiryProducts(LocalDate currentDate, LocalDate thresholdDate);

    @Query("{ 'available_quantity': { $lte: ?0 }, 'active': true }")
    List<Product> findLowStockProducts(int threshold);

    List<Product> findByCategoryAndActiveTrue(ProductCategory category);

    List<Product> findByProductTypeAndActiveTrue(ProductType productType);

    boolean existsBySku(String sku);

    @Query("{ 'batch_number': ?0 }")
    List<Product> findByBatchNumber(String batchNumber);
}
