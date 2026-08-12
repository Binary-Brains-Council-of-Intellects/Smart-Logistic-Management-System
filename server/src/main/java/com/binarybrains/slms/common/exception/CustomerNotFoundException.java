package com.binarybrains.slms.common.exception;

public class CustomerNotFoundException extends ResourceNotFoundException {

    public CustomerNotFoundException(String customerId) {
        super("Customer", "id", customerId);
    }
}
