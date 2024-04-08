package com.example.ecommerce.service;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setId(1L);
        product1.setName("Test Product 1");
        product1.setDescription("Test Description 1");
        product1.setPrice(new BigDecimal("19.99"));
        product1.setQuantity(10);

        product2 = new Product();
        product2.setId(2L);
        product2.setName("Test Product 2");
        product2.setDescription("Test Description 2");
        product2.setPrice(new BigDecimal("29.99"));
        product2.setQuantity(5);
    }

    @Test
    void findAllProducts_ShouldReturnAllProducts() {
        // Arrange
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = productService.findAllProducts();

        // Assert
        assertEquals(2, result.size());
        assertEquals(product1.getName(), result.get(0).getName());
        assertEquals(product2.getName(), result.get(1).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void findProductById_ExistingId_ShouldReturnProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        // Act
        Optional<Product> result = productService.findProductById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(product1.getName(), result.get().getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void findProductById_NonExistingId_ShouldReturnEmpty() {
        // Arrange
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Product> result = productService.findProductById(99L);

        // Assert
        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById(99L);
    }

    @Test
    void saveProduct_ShouldReturnSavedProduct() {
        // Arrange
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        // Act
        Product savedProduct = productService.saveProduct(product1);

        // Assert
        assertNotNull(savedProduct);
        assertEquals(product1.getName(), savedProduct.getName());
        verify(productRepository, times(1)).save(product1);
    }

    @Test
    void deleteProduct_ShouldCallRepositoryDelete() {
        // Arrange
        doNothing().when(productRepository).deleteById(1L);

        // Act
        productService.deleteProduct(1L);

        // Assert
        verify(productRepository, times(1)).deleteById(1L);
    }
}
