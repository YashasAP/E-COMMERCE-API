package com.mystore.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mystore.entity.Cart;
import com.mystore.entity.CartItem;
import com.mystore.entity.Order;
import com.mystore.entity.OrderItem;
import com.mystore.entity.Product;
import com.mystore.entity.User;
import com.mystore.exception.ResourceNotFoundException;
import com.mystore.repository.CartItemRepository;
import com.mystore.repository.CartRepository;
import com.mystore.repository.OrderItemRepository;
import com.mystore.repository.OrderRepository;
import com.mystore.repository.ProductRepository;
import com.mystore.repository.UserRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Order createOrderFromCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + username));

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot create an order from an empty cart.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        double totalAmount = 0.0;

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();

            if (product.getStock() < quantity) {
                throw new IllegalArgumentException("Not enough stock for product: " + product.getName() + ". Available: " + product.getStock() + ", Requested: " + quantity);
            }

            // Decrease product stock
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);

            OrderItem orderItem = new OrderItem(order, product, quantity, product.getPrice());
            order.getOrderItems().add(orderItem);
            totalAmount += product.getPrice() * quantity;
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);
        orderItemRepository.saveAll(order.getOrderItems()); // Save order items

        // Clear the cart after order creation
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear(); // Clear the collection in the entity
        cartRepository.save(cart); // Persist the cleared cart

        return savedOrder;
    }

    public List<Order> getOrdersByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return orderRepository.findByUser(user);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }
}