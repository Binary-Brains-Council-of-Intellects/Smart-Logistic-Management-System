package com.binarybrains.slms.returns.service;

import com.binarybrains.slms.common.exception.*;
import com.binarybrains.slms.customer.repository.CustomerRepository;
import com.binarybrains.slms.inventory.model.Product;
import com.binarybrains.slms.inventory.repository.ProductRepository;
import com.binarybrains.slms.order.repository.OrderRepository;
import com.binarybrains.slms.returns.dto.CreateReturnRequest;
import com.binarybrains.slms.returns.dto.ReturnResponse;
import com.binarybrains.slms.returns.model.ReturnRequest;
import com.binarybrains.slms.returns.model.ReturnStatus;
import com.binarybrains.slms.returns.repository.ReturnRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Return/exchange service.
 *
 * Business rules:
 *   - DAMAGED products are NOT added back to sellable inventory
 *   - EXPIRED products are NOT added back to sellable inventory
 *   - WRONG_ITEM and CUSTOMER_CHANGED_MIND items ARE restored to stock
 */
@Service
public class ReturnService {

    private final ReturnRepository returnRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public ReturnService(ReturnRepository returnRepository, OrderRepository orderRepository,
                         CustomerRepository customerRepository, ProductRepository productRepository) {
        this.returnRepository = returnRepository;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public ReturnResponse createReturnRequest(CreateReturnRequest request) {
        // Validate order exists
        var order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(request.getOrderId()));

        var customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.getCustomerId()));

        var product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(request.getProductId()));

        // Validate quantity doesn't exceed ordered quantity
        var orderedItem = order.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new InvalidReturnException(
                        "Product '" + request.getProductId() + "' was not part of order '" + request.getOrderId() + "'"));

        if (request.getQuantity() > orderedItem.getQuantity()) {
            throw new InvalidReturnException("Return quantity exceeds ordered quantity");
        }

        ReturnRequest returnRequest = new ReturnRequest(
                request.getOrderId(), customer.getId(), customer.getName(),
                product.getProductId(), product.getName(),
                request.getQuantity(), request.getReason(), request.getNotes()
        );

        return ReturnResponse.fromReturnRequest(returnRepository.save(returnRequest));
    }

    public ReturnResponse approveReturn(String id) {
        ReturnRequest returnRequest = findOrThrow(id);
        validateStatusTransition(returnRequest, ReturnStatus.APPROVED);
        returnRequest.setStatus(ReturnStatus.APPROVED);
        returnRequest.setResolvedDate(LocalDateTime.now());
        return ReturnResponse.fromReturnRequest(returnRepository.save(returnRequest));
    }

    public ReturnResponse rejectReturn(String id) {
        ReturnRequest returnRequest = findOrThrow(id);
        validateStatusTransition(returnRequest, ReturnStatus.REJECTED);
        returnRequest.setStatus(ReturnStatus.REJECTED);
        returnRequest.setResolvedDate(LocalDateTime.now());
        return ReturnResponse.fromReturnRequest(returnRepository.save(returnRequest));
    }

    /**
     * Completes a return and conditionally restores inventory.
     * DAMAGED/EXPIRED items do NOT get added back.
     */
    public ReturnResponse completeReturn(String id) {
        ReturnRequest returnRequest = findOrThrow(id);

        if (returnRequest.getStatus() != ReturnStatus.APPROVED) {
            throw new InvalidReturnException("Only approved returns can be completed");
        }

        returnRequest.setStatus(ReturnStatus.COMPLETED);
        returnRequest.setResolvedDate(LocalDateTime.now());

        // Only restore stock for non-damaged, non-expired returns
        if (returnRequest.canRestoreToInventory()) {
            productRepository.findById(returnRequest.getProductId()).ifPresent(product -> {
                product.addStock(returnRequest.getQuantity());
                productRepository.save(product);
            });
        }

        return ReturnResponse.fromReturnRequest(returnRepository.save(returnRequest));
    }

    public ReturnResponse getReturnById(String id) {
        return ReturnResponse.fromReturnRequest(findOrThrow(id));
    }

    public List<ReturnResponse> getAllReturns() {
        return returnRepository.findAll().stream().map(ReturnResponse::fromReturnRequest).collect(Collectors.toList());
    }

    public List<ReturnResponse> getReturnsByCustomer(String customerId) {
        return returnRepository.findByCustomerId(customerId).stream().map(ReturnResponse::fromReturnRequest).collect(Collectors.toList());
    }

    public List<ReturnResponse> getReturnsByOrder(String orderId) {
        return returnRepository.findByOrderId(orderId).stream().map(ReturnResponse::fromReturnRequest).collect(Collectors.toList());
    }

    public List<ReturnResponse> getReturnsByStatus(ReturnStatus status) {
        return returnRepository.findByStatus(status).stream().map(ReturnResponse::fromReturnRequest).collect(Collectors.toList());
    }

    private ReturnRequest findOrThrow(String id) {
        return returnRepository.findById(id)
                .orElseThrow(() -> new InvalidReturnException("Return request not found: " + id));
    }

    private void validateStatusTransition(ReturnRequest r, ReturnStatus newStatus) {
        if (r.getStatus() != ReturnStatus.REQUESTED) {
            throw new InvalidReturnException("Return can only be " + newStatus + " from REQUESTED status");
        }
    }
}
