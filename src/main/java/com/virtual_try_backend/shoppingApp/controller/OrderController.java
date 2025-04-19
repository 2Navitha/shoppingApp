package com.virtual_try_backend.shoppingApp.controller;


import com.virtual_try_backend.shoppingApp.entity.Order;
import com.virtual_try_backend.shoppingApp.entity.User;
import com.virtual_try_backend.shoppingApp.repository.OrderRepository;
import com.virtual_try_backend.shoppingApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get orders by user
    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // Place new order
    @PostMapping("/place/{userId}")
    public Order placeOrder(@PathVariable Long userId, @RequestBody Order order) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            order.setUser(userOptional.get());
            order.setOrderDate(new Date());
            order.setStatus("PLACED");
            return orderRepository.save(order);
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }

    // Update order status
    @PutMapping("/{orderId}")
    public Order updateOrder(@PathVariable Long orderId, @RequestBody Order updatedOrder) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(updatedOrder.getStatus());
        return orderRepository.save(order);
    }

    // Delete order
    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
