package com.binarybrains.slms.review.controller;

import com.binarybrains.slms.common.response.ApiResponse;
import com.binarybrains.slms.review.dto.CreateReviewRequest;
import com.binarybrains.slms.review.dto.ReviewResponse;
import com.binarybrains.slms.review.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Review Management", description = "Customer feedback and product reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> create(@Valid @RequestBody CreateReviewRequest req) {
        return new ResponseEntity<>(ApiResponse.success("Review submitted", reviewService.createReview(req)), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getAllReviews()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewById(id)));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getByProduct(@PathVariable String productId) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewsByProduct(productId)));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewsByCustomer(customerId)));
    }

    @GetMapping("/product/{productId}/average-rating")
    public ResponseEntity<ApiResponse<Double>> getAverageRating(@PathVariable String productId) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getAverageRatingForProduct(productId)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        reviewService.deleteReview(id); return ResponseEntity.ok(ApiResponse.success("Deleted", null));
    }
}
