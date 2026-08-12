package com.binarybrains.slms.returns.repository;

import com.binarybrains.slms.returns.model.ReturnRequest;
import com.binarybrains.slms.returns.model.ReturnStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnRepository extends MongoRepository<ReturnRequest, String> {
    List<ReturnRequest> findByCustomerId(String customerId);
    List<ReturnRequest> findByOrderId(String orderId);
    List<ReturnRequest> findByStatus(ReturnStatus status);
    List<ReturnRequest> findByProductId(String productId);
    long countByStatus(ReturnStatus status);
}
