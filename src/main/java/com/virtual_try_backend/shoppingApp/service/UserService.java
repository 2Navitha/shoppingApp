package com.virtual_try_backend.shoppingApp.service;

import com.virtual_try_backend.shoppingApp.config.JwtUtil;
import com.virtual_try_backend.shoppingApp.dto.LoginRequest;
import com.virtual_try_backend.shoppingApp.dto.SignupRequest;
import com.virtual_try_backend.shoppingApp.entity.User;
import com.virtual_try_backend.shoppingApp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Register new user (Local)
    public String registerUser(SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use!");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // ✅ Password Hashing
        user.setRole("ROLE_USER");
        user.setProvider("LOCAL");

        userRepository.save(user);
        return "User registered successfully!";
    }

    // ✅ Login user (Local)
    public String loginUser(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return jwtUtil.generateToken(user.getEmail()); // ✅ Secure JWT Token Generation
            }
        }

        throw new RuntimeException("Invalid email or password!");
    }

    // ✅ Google OAuth2 Signup & Login
    public String registerOrUpdateOAuth2User(String email, String fullName) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;

        if (existingUser.isPresent()) {
            user = existingUser.get();
            user.setFullName(fullName); // ✅ Update name if changed
        } else {
            user = new User();
            user.setEmail(email);
            user.setFullName(fullName);
            user.setRole("ROLE_USER");
            user.setProvider("GOOGLE"); // ✅ Set OAuth provider
            userRepository.save(user);
        }

        return jwtUtil.generateToken(user.getEmail()); // ✅ Return JWT for OAuth user
    }
}
