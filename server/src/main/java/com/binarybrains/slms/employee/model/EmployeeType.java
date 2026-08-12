package com.binarybrains.slms.employee.model;

/**
 * Enum representing employee types in the system.
 * Used by EmployeeFactory and MongoDB discriminator.
 */
public enum EmployeeType {
    WAREHOUSE_STAFF,
    DELIVERY_DRIVER,
    MANAGER
}
