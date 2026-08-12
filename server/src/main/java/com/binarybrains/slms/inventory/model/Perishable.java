package com.binarybrains.slms.inventory.model;

import java.time.LocalDate;

/**
 * ====================================================================
 * INTERFACE: Perishable
 * ====================================================================
 * OOP Concept: ABSTRACTION + INTERFACE-BASED POLYMORPHISM
 *
 * This interface represents products that have expiry-related behavior.
 * Any product that can expire must implement this interface.
 *
 * PerishableProduct implements Perishable, while NonPerishableProduct does not.
 * This allows the system to check:
 *   if (product instanceof Perishable) { ... }
 * demonstrating interface-based polymorphism at runtime.
 * ====================================================================
 */
public interface Perishable {

    /**
     * Checks if the product is approaching its expiry date.
     *
     * @param days the number of days threshold for "near expiry"
     * @return true if the product will expire within the given number of days
     */
    boolean isNearExpiry(int days);

    /**
     * Returns the expiration date of the product.
     *
     * @return the expiration date
     */
    LocalDate getExpirationDate();

    /**
     * Checks whether the product has expired.
     *
     * @return true if the product is past its expiry date
     */
    boolean checkExpiry();
}
