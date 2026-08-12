package com.binarybrains.slms.customer.repository;

import com.binarybrains.slms.customer.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
    Optional<Customer> findByEmail(String email);
    List<Customer> findByActiveTrue();
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Customer> searchByName(String keyword);
    boolean existsByEmail(String email);
    long countByActiveTrue();
}
