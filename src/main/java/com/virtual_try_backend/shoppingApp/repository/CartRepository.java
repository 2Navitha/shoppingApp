package com.virtual_try_backend.shoppingApp.repository;

import com.virtual_try_backend.shoppingApp.entity.CartItem;
import com.virtual_try_backend.shoppingApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {

    // Custom query to find cart item by user and product id
    @Query("SELECT c FROM CartItem c WHERE c.user = :user AND c.product.id = :productId")
    CartItem findByUserAndProductId(@Param("user") User user, @Param("productId") Long productId);

    // Method to find all cart items for a user
    List<CartItem> findByUser(User user);

    // Method to delete a cart item by user and product ID
    void deleteByUserAndProductId(User user, Long productId);
}
