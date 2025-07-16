package com.mystore.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mystore.entity.Cart;
import com.mystore.entity.CartItem;
import com.mystore.entity.Product;
import com.mystore.entity.User;
import com.mystore.exception.ResourceNotFoundException;
import com.mystore.repository.CartItemRepository;
import com.mystore.repository.CartRepository;
import com.mystore.repository.ProductRepository;
import com.mystore.repository.UserRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public Cart getCartByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + username));
    }

    @Transactional
    public CartItem addProductToCart(String username, Long productId, Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }

        Cart cart = getCartByUser(username);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
        }

        Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProduct(cart, product);

        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            // Check if adding more quantity exceeds stock
            if (product.getStock() < (cartItem.getQuantity() + quantity)) {
                throw new IllegalArgumentException("Adding " + quantity + " units would exceed available stock for " + product.getName());
            }
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem(cart, product, quantity);
            cart.getCartItems().add(cartItem); // Add to cart's item list for consistency
        }

        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public CartItem updateCartItemQuantity(String username, Long cartItemId, Integer newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }

        Cart cart = getCartByUser(username);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("Cart item does not belong to the current user's cart.");
        }

        Product product = cartItem.getProduct();
        if (product.getStock() < newQuantity) {
            throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
        }

        if (newQuantity == 0) {
            cartItemRepository.delete(cartItem);
            return null; // Indicate item was removed
        } else {
            cartItem.setQuantity(newQuantity);
            return cartItemRepository.save(cartItem);
        }
    }

    @Transactional
    public void removeProductFromCart(String username, Long cartItemId) {
        Cart cart = getCartByUser(username);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("Cart item does not belong to the current user's cart.");
        }

        cartItemRepository.delete(cartItem);
    }
}