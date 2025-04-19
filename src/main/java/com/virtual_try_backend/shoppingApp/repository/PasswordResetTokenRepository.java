package com.virtual_try_backend.shoppingApp.repository;


import com.virtual_try_backend.shoppingApp.entity.PasswordResetToken;
import com.virtual_try_backend.shoppingApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    Optional<PasswordResetToken> findByUser(User user);
}
