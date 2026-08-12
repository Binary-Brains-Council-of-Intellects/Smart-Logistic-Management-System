package com.binarybrains.slms.common.exception;

public class OrderNotFoundException extends ResourceNotFoundException {

    public OrderNotFoundException(String orderId) {
        super("Order", "id", orderId);
    }
}
