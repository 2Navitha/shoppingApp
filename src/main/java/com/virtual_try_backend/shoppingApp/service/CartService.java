package com.virtual_try_backend.shoppingApp.service;

import com.virtual_try_backend.shoppingApp.entity.CartItem;
import com.virtual_try_backend.shoppingApp.entity.Product;
import com.virtual_try_backend.shoppingApp.entity.User;
import com.virtual_try_backend.shoppingApp.repository.CartRepository;
import com.virtual_try_backend.shoppingApp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    // Add item to cart
    public void addToCart(User user, Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Find the cart item for the user and product using the updated method
        CartItem cartItem = cartRepository.findByUserAndProductId(user, product.getId());

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);  // Set the fetched product
            cartItem.setQuantity(quantity);
        }

        cartRepository.save(cartItem);
    }

    // Get cart items for a user
    public List<CartItem> getCartItems(User user) {
        return cartRepository.findByUser(user);
    }

    // Remove item from cart
    public void removeFromCart(User user, Long productId) {
        cartRepository.deleteByUserAndProductId(user, productId);
    }

    // Update cart item quantity
    public void updateCartItemQuantity(User user, Long productId, int quantity) {
        CartItem cartItem = cartRepository.findByUserAndProductId(user, productId);
        if (cartItem != null) {
            cartItem.setQuantity(quantity);
            cartRepository.save(cartItem);
        }
    }
}
