package com.mystore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mystore.entity.Cart;
import com.mystore.entity.User;
import com.mystore.enums.UserRole;
import com.mystore.repository.CartRepository;
import com.mystore.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User registerNewUser(String username, String password, UserRole role) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        User savedUser = userRepository.save(user);

        // Create a cart for the new customer
        if (role == UserRole.CUSTOMER) {
            Cart cart = new Cart(savedUser);
            cartRepository.save(cart);
            savedUser.setCart(cart); // Link cart back to user
            userRepository.save(savedUser); // Update user to reflect cart
        }

        return savedUser;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }
}