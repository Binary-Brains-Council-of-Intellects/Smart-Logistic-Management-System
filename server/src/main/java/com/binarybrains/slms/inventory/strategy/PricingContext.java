package com.binarybrains.slms.inventory.strategy;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ====================================================================
 * STRATEGY PATTERN: PricingContext
 * ====================================================================
 * Context class that resolves and delegates to the appropriate
 * PricingStrategy at runtime.
 *
 * Spring injects all PricingStrategy beans via constructor injection.
 * The context stores them in a map keyed by strategy name.
 *
 * Usage:
 *   PricingStrategy strategy = pricingContext.getStrategy("WHOLESALE");
 *   double price = strategy.calculatePrice(basePrice, quantity);
 * ====================================================================
 */
@Component
public class PricingContext {

    private final Map<String, PricingStrategy> strategyMap;
    private final PricingStrategy defaultStrategy;

    /**
     * Spring injects ALL beans implementing PricingStrategy.
     * This demonstrates Spring's IoC collecting multiple implementations.
     */
    public PricingContext(List<PricingStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(PricingStrategy::getStrategyName, Function.identity()));
        this.defaultStrategy = strategyMap.getOrDefault("REGULAR", strategies.get(0));
    }

    /**
     * Resolves the pricing strategy by name.
     *
     * @param strategyName the strategy name (REGULAR, WHOLESALE, DISCOUNT)
     * @return the matching strategy, or REGULAR as default
     */
    public PricingStrategy getStrategy(String strategyName) {
        if (strategyName == null) {
            return defaultStrategy;
        }
        return strategyMap.getOrDefault(strategyName.toUpperCase(), defaultStrategy);
    }

    /**
     * Calculates price using the named strategy.
     */
    public double calculatePrice(String strategyName, double basePrice, int quantity) {
        PricingStrategy strategy = getStrategy(strategyName);
        return strategy.calculatePrice(basePrice, quantity);
    }
}
