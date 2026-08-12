package com.binarybrains.slms.model;

import com.binarybrains.slms.inventory.model.NonPerishableProduct;
import com.binarybrains.slms.inventory.model.PerishableProduct;
import com.binarybrains.slms.inventory.model.ProductCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product Model Domain Unit Tests")
class ProductModelTest {

    @Test
    @DisplayName("Should correctly calculate profit margin and stock dispatch")
    void testStockOperationsAndProfitMargin() {
        NonPerishableProduct product = new NonPerishableProduct(
                "Scanner", "SKU-001", "Barcode reader", ProductCategory.ELECTRONICS,
                "B1", LocalDate.now(), 100, 50.0, 100.0, 12, 0.5, false
        );

        assertEquals(100.0, product.calculateProfitMargin(), 0.01);
        assertEquals(100, product.getAvailableQuantity());

        product.dispatch(20);
        assertEquals(80, product.getAvailableQuantity());

        product.addStock(30);
        assertEquals(110, product.getAvailableQuantity());
        assertEquals(130, product.getTotalQuantity());

        assertThrows(IllegalStateException.class, () -> product.dispatch(200));
        assertThrows(IllegalArgumentException.class, () -> product.dispatch(-10));
    }

    @Test
    @DisplayName("Should verify perishable product expiry logic and polymorphic storage requirement")
    void testPerishableProductExpiryAndStorage() {
        PerishableProduct fresh = new PerishableProduct(
                "Milk", "SKU-MILK", "Fresh milk", ProductCategory.DAIRY,
                "B2", LocalDate.now().minusDays(2), LocalDate.now().plusDays(10),
                50, 1.0, 2.0, 4.0, true
        );

        PerishableProduct expired = new PerishableProduct(
                "Old Milk", "SKU-OLD", "Expired milk", ProductCategory.DAIRY,
                "B3", LocalDate.now().minusDays(20), LocalDate.now().minusDays(1),
                50, 1.0, 2.0, 4.0, true
        );

        assertFalse(fresh.isExpired());
        assertTrue(fresh.isNearExpiry(15));
        assertFalse(fresh.isNearExpiry(5));

        assertTrue(expired.isExpired());
        assertTrue(expired.checkExpiry());

        assertTrue(fresh.calculateStorageRequirement().contains("COLD STORAGE"));
    }

    @Test
    @DisplayName("Should verify non-perishable storage requirement override")
    void testNonPerishableStorageRequirement() {
        NonPerishableProduct fragile = new NonPerishableProduct(
                "Glassware", "SKU-GLASS", "Fragile cups", ProductCategory.HOUSEHOLD,
                "B4", LocalDate.now(), 30, 5.0, 15.0, 0, 1.0, true
        );

        assertTrue(fragile.calculateStorageRequirement().contains("FRAGILE"));
    }
}
