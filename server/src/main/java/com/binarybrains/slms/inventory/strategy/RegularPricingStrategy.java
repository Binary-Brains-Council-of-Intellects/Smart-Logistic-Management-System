package com.binarybrains.slms.inventory.strategy;

import org.springframework.stereotype.Component;

/**
 * Regular pricing — no discounts applied.
 */
@Component
public class RegularPricingStrategy implements PricingStrategy {

    @Override
    public double calculatePrice(double basePrice, int quantity) {
        return basePrice * quantity;
    }

    @Override
    public String getStrategyName() {
        return "REGULAR";
    }
}
