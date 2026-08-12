package com.binarybrains.slms.common.exception;

/**
 * Thrown when an operation is attempted on an expired product.
 */
public class ExpiredProductException extends RuntimeException {

    private final String productId;

    public ExpiredProductException(String productId) {
        super(String.format("Product '%s' has expired and cannot be fulfilled", productId));
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
