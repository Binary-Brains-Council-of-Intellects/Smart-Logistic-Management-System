package com.binarybrains.slms.inventory;

import com.binarybrains.slms.common.exception.InsufficientStockException;
import com.binarybrains.slms.common.exception.ProductNotFoundException;
import com.binarybrains.slms.inventory.dto.CreateProductRequest;
import com.binarybrains.slms.inventory.dto.ProductResponse;
import com.binarybrains.slms.inventory.factory.ProductFactory;
import com.binarybrains.slms.inventory.model.NonPerishableProduct;
import com.binarybrains.slms.inventory.model.Product;
import com.binarybrains.slms.inventory.model.ProductCategory;
import com.binarybrains.slms.inventory.model.ProductType;
import com.binarybrains.slms.inventory.repository.ProductRepository;
import com.binarybrains.slms.inventory.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Mockito Unit Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductFactory productFactory = new ProductFactory();

    private ProductServiceImpl productService;

    private CreateProductRequest createRequest;
    private NonPerishableProduct testProduct;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository, productFactory);
        createRequest = new CreateProductRequest();
        createRequest.setName("Test Laptop");
        createRequest.setSku("LAP-001");
        createRequest.setCategory(ProductCategory.ELECTRONICS);
        createRequest.setProductType(ProductType.NON_PERISHABLE);
        createRequest.setQuantity(50);
        createRequest.setCostPrice(500.0);
        createRequest.setSellingPrice(800.0);

        testProduct = new NonPerishableProduct();
        testProduct.setProductId("PROD-123");
        testProduct.setName("Test Laptop");
        testProduct.setSku("LAP-001");
        testProduct.setCategory(ProductCategory.ELECTRONICS);
        testProduct.setProductType(ProductType.NON_PERISHABLE);
        testProduct.addStock(50);
        testProduct.setCostPrice(500.0);
        testProduct.setSellingPrice(800.0);
    }

    @Test
    @DisplayName("Should successfully create product using Factory Method pattern")
    void testCreateProduct() {
        when(productRepository.existsBySku("LAP-001")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductResponse response = productService.createProduct(createRequest);

        assertNotNull(response);
        assertEquals("LAP-001", response.getSku());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should deduct stock successfully when sufficient inventory exists")
    void testDeductStockSuccess() {
        when(productRepository.findById("PROD-123")).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductResponse response = productService.deductStock("PROD-123", 20);

        assertNotNull(response);
        assertEquals(30, testProduct.getAvailableQuantity());
        verify(productRepository).save(testProduct);
    }

    @Test
    @DisplayName("Should throw InsufficientStockException when requesting more than available")
    void testDeductStockInsufficient() {
        when(productRepository.findById("PROD-123")).thenReturn(Optional.of(testProduct));

        assertThrows(InsufficientStockException.class, () -> productService.deductStock("PROD-123", 100));
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when product ID is invalid")
    void testGetProductNotFound() {
        when(productRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById("INVALID"));
    }
}
