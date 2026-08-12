package com.binarybrains.slms.inventory.model;

/**
 * Enum representing product types in the system.
 * Used by the Factory Method pattern to determine which
 * product subclass to instantiate.
 */
public enum ProductType {
    PERISHABLE,
    NON_PERISHABLE
}
