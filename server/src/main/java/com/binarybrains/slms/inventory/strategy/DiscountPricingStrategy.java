package com.binarybrains.slms.inventory.strategy;

import org.springframework.stereotype.Component;

/**
 * Discount pricing — flat 15% discount on all items.
 */
@Component
public class DiscountPricingStrategy implements PricingStrategy {

    private static final double DISCOUNT_PERCENTAGE = 0.15;

    @Override
    public double calculatePrice(double basePrice, int quantity) {
        return basePrice * quantity * (1 - DISCOUNT_PERCENTAGE);
    }

    @Override
    public String getStrategyName() {
        return "DISCOUNT";
    }
}
