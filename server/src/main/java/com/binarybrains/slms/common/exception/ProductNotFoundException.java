package com.binarybrains.slms.common.exception;

public class ProductNotFoundException extends ResourceNotFoundException {

    public ProductNotFoundException(String productId) {
        super("Product", "id", productId);
    }
}
