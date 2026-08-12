package com.binarybrains.slms.inventory.strategy;

/**
 * ====================================================================
 * STRATEGY PATTERN: PricingStrategy Interface
 * ====================================================================
 * Defines a family of pricing algorithms.
 * The OrderService can switch strategies without changing its own logic.
 *
 *   PricingStrategy strategy = pricingContext.getStrategy("WHOLESALE");
 *   double price = strategy.calculatePrice(basePrice, quantity);
 *
 * Demonstrates behavioral polymorphism through interfaces.
 * ====================================================================
 */
public interface PricingStrategy {

    /**
     * Calculates the final price for a given base price and quantity.
     */
    double calculatePrice(double basePrice, int quantity);

    /**
     * Returns the name of this pricing strategy.
     */
    String getStrategyName();
}
