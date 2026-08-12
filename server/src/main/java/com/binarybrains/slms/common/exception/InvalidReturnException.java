package com.binarybrains.slms.common.exception;

/**
 * Thrown when a return request violates business rules.
 */
public class InvalidReturnException extends RuntimeException {

    public InvalidReturnException(String message) {
        super(message);
    }
}
