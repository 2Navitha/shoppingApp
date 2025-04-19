package com.virtual_try_backend.shoppingApp.repository;

import com.virtual_try_backend.shoppingApp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);
}
