package com.example.ecommerce.integration;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        // Clear the repository before each test
        productRepository.deleteAll();

        // Create and save a test product
        testProduct = new Product();
        testProduct.setName("Integration Test Product");
        testProduct.setDescription("Product for integration testing");
        testProduct.setPrice(new BigDecimal("24.99"));
        testProduct.setQuantity(15);

        testProduct = productRepository.save(testProduct);
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        productRepository.deleteAll();
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(testProduct.getName())))
                .andExpect(jsonPath("$[0].price", is(testProduct.getPrice().doubleValue())));
    }

    @Test
    void getProductById_ExistingId_ShouldReturnProduct() throws Exception {
        mockMvc.perform(get("/api/products/{id}", testProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(testProduct.getName())))
                .andExpect(jsonPath("$.description", is(testProduct.getDescription())));
    }

    @Test
    void getProductById_NonExistingId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        Product newProduct = new Product();
        newProduct.setName("New Integration Test Product");
        newProduct.setDescription("New product description");
        newProduct.setPrice(new BigDecimal("34.99"));
        newProduct.setQuantity(20);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(newProduct.getName())))
                .andExpect(jsonPath("$.price", is(newProduct.getPrice().doubleValue())));
    }

    @Test
    void updateProduct_ExistingId_ShouldReturnUpdatedProduct() throws Exception {
        // Update product details
        testProduct.setName("Updated Product Name");
        testProduct.setPrice(new BigDecimal("39.99"));

        mockMvc.perform(put("/api/products/{id}", testProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Product Name")))
                .andExpect(jsonPath("$.price", is(39.99)));
    }

    @Test
    void deleteProduct_ExistingId_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/products/{id}", testProduct.getId()))
                .andExpect(status().isNoContent());

        // Verify product was deleted
        mockMvc.perform(get("/api/products/{id}", testProduct.getId()))
                .andExpect(status().isNotFound());
    }
}
