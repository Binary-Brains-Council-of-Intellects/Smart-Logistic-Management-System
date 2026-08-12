package com.binarybrains.slms.inventory.strategy;

import org.springframework.stereotype.Component;

/**
 * Wholesale pricing — bulk quantity discounts.
 * 50+ units: 10% off
 * 100+ units: 20% off
 */
@Component
public class WholesalePricingStrategy implements PricingStrategy {

    @Override
    public double calculatePrice(double basePrice, int quantity) {
        double discount;
        if (quantity >= 100) {
            discount = 0.20;
        } else if (quantity >= 50) {
            discount = 0.10;
        } else {
            discount = 0.0;
        }
        return basePrice * quantity * (1 - discount);
    }

    @Override
    public String getStrategyName() {
        return "WHOLESALE";
    }
}
