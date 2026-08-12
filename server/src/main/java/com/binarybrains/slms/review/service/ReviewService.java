package com.binarybrains.slms.review.service;

import com.binarybrains.slms.common.exception.CustomerNotFoundException;
import com.binarybrains.slms.common.exception.ProductNotFoundException;
import com.binarybrains.slms.customer.repository.CustomerRepository;
import com.binarybrains.slms.inventory.repository.ProductRepository;
import com.binarybrains.slms.review.dto.CreateReviewRequest;
import com.binarybrains.slms.review.dto.ReviewResponse;
import com.binarybrains.slms.review.model.Review;
import com.binarybrains.slms.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public ReviewService(ReviewRepository reviewRepository, CustomerRepository customerRepository,
                         ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public ReviewResponse createReview(CreateReviewRequest request) {
        var customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.getCustomerId()));
        var product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(request.getProductId()));

        Review review = new Review(customer.getId(), customer.getName(),
                product.getProductId(), product.getName(), request.getRating(), request.getComment());
        return ReviewResponse.fromReview(reviewRepository.save(review));
    }

    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll().stream().map(ReviewResponse::fromReview).collect(Collectors.toList());
    }

    public List<ReviewResponse> getReviewsByProduct(String productId) {
        return reviewRepository.findByProductId(productId).stream().map(ReviewResponse::fromReview).collect(Collectors.toList());
    }

    public List<ReviewResponse> getReviewsByCustomer(String customerId) {
        return reviewRepository.findByCustomerId(customerId).stream().map(ReviewResponse::fromReview).collect(Collectors.toList());
    }

    public double getAverageRatingForProduct(String productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        if (reviews.isEmpty()) return 0.0;
        return reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
    }

    public ReviewResponse getReviewById(String id) {
        return ReviewResponse.fromReview(reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found: " + id)));
    }

    public void deleteReview(String id) {
        reviewRepository.deleteById(id);
    }
}
