package com.virtual_try_backend.shoppingApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")  // Maps to the User entity
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")  // Maps to the Product entity
    private Product product;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")  // Link to an order (nullable until the item is ordered)
    private Order order;

    // You can include additional methods here if necessary, such as logic to convert a CartItem to an OrderItem
}
