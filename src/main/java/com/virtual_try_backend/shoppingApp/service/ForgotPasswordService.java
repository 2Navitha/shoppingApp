package com.virtual_try_backend.shoppingApp.service;

import com.virtual_try_backend.shoppingApp.entity.PasswordResetToken;
import com.virtual_try_backend.shoppingApp.entity.User;
import com.virtual_try_backend.shoppingApp.repository.PasswordResetTokenRepository;
import com.virtual_try_backend.shoppingApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ForgotPasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetLink(String email) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found"));

        String token = UUID.randomUUID().toString();

        // Check if a token already exists for this user
        Optional<PasswordResetToken> existingTokenOpt = tokenRepository.findByUser(user);
        PasswordResetToken resetToken;

        if (existingTokenOpt.isPresent()) {
            resetToken = existingTokenOpt.get();
        } else {
            resetToken = new PasswordResetToken();
            resetToken.setUser(user);
        }

        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        tokenRepository.save(resetToken);

        String resetLink = "http://localhost:5500/ResetPassword.html?token=" + token;


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Reset Password Request");
        message.setText("Click the link to reset your password: " + resetLink);

        mailSender.send(message);
    }

    public void resetPassword(String token, String newPassword) throws Exception {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);

        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new Exception("Invalid or expired token");
        }

        User user = resetToken.getUser();
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken); // invalidate token after use
    }
}
