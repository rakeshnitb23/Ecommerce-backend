package com.example.ecommerce.service;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private User user;
    private Product product;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        // Set up test data
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(new BigDecimal("29.99"));
        product.setQuantity(10);

        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setProduct(product);
        orderItem.setQuantity(2);
        orderItem.setPrice(new BigDecimal("59.98")); // 29.99 * 2

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setOrderItems(orderItems);
        order.setTotalAmount(new BigDecimal("59.98"));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
    }

    @Test
    void findAllOrders_ShouldReturnAllOrders() {
        // Arrange
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        when(orderRepository.findAll()).thenReturn(orders);

        // Act
        List<Order> result = orderService.findAllOrders();

        // Assert
        assertEquals(1, result.size());
        assertEquals(order.getId(), result.get(0).getId());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void findOrderById_ExistingId_ShouldReturnOrder() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Act
        Optional<Order> result = orderService.findOrderById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(order.getId(), result.get().getId());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void findOrderById_NonExistingId_ShouldReturnEmpty() {
        // Arrange
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Order> result = orderService.findOrderById(99L);

        // Assert
        assertFalse(result.isPresent());
        verify(orderRepository, times(1)).findById(99L);
    }

    @Test
    void findOrdersByUserId_ShouldReturnUserOrders() {
        // Arrange
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        when(orderRepository.findByUserId(1L)).thenReturn(orders);

        // Act
        List<Order> result = orderService.findOrdersByUserId(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(order.getId(), result.get(0).getId());
        verify(orderRepository, times(1)).findByUserId(1L);
    }

    @Test
    void createOrder_ShouldReturnSavedOrder() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        Order savedOrder = orderService.createOrder(order);

        // Assert
        assertNotNull(savedOrder);
        assertEquals(order.getId(), savedOrder.getId());
        assertEquals(order.getTotalAmount(), savedOrder.getTotalAmount());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void updateOrderStatus_ShouldUpdateStatus() {
        // Arrange
        Order existingOrder = order;
        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order updatedOrder = orderService.updateOrderStatus(1L, "SHIPPED");

        // Assert
        assertNotNull(updatedOrder);
        assertEquals("SHIPPED", updatedOrder.getStatus());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}
