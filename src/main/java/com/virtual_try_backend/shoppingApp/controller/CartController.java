package com.virtual_try_backend.shoppingApp.controller;

import com.virtual_try_backend.shoppingApp.entity.CartItem;
import com.virtual_try_backend.shoppingApp.entity.User;
import com.virtual_try_backend.shoppingApp.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Add item to cart
    @PostMapping("/add")
    public void addToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        // Assuming User with userId exists, you'll fetch the user here
        User user = new User();
        user.setId(userId);  // Fetch the actual user from the DB based on userId
        cartService.addToCart(user, productId, quantity);
    }

    // Get all cart items for a user
    @GetMapping("/items")
    public List<CartItem> getCartItems(@RequestParam Long userId) {
        User user = new User();
        user.setId(userId);  // Fetch the actual user from the DB
        return cartService.getCartItems(user);
    }

    // Remove item from cart
    @DeleteMapping("/remove")
    public void removeFromCart(@RequestParam Long userId, @RequestParam Long productId) {
        User user = new User();
        user.setId(userId);  // Fetch the actual user from the DB
        cartService.removeFromCart(user, productId);
    }

    // Update item quantity in the cart
    @PutMapping("/update")
    public void updateCartItemQuantity(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        User user = new User();
        user.setId(userId);  // Fetch the actual user from the DB
        cartService.updateCartItemQuantity(user, productId, quantity);
    }
}
