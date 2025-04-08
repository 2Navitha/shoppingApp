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

    /**
     * ✅ Register a new user using email and password
     * @param request SignupRequest containing user details
     * @return Success message
     */
    public String registerUser(SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use!");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Hash password
        user.setRole("ROLE_USER");
        user.setProvider("LOCAL");

        userRepository.save(user);
        return "User registered successfully!";
    }

    /**
     * ✅ Login user using email and password
     * @param request LoginRequest with email and password
     * @return JWT token on successful login
     */
    public String loginUser(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return jwtUtil.generateToken(user.getEmail()); // Generate JWT token
            }
        }

        throw new RuntimeException("Invalid email or password!");
    }

    /**
     * ✅ Register or update user using Google OAuth2
     * @param email User's email from Google
     * @param fullName User's name from Google
     * @return JWT token for Google-authenticated user
     */
    public String registerOrUpdateOAuth2User(String email, String fullName) {
        User user = userRepository.findByEmail(email)
                .map(existingUser -> {
                    existingUser.setFullName(fullName); // Update name if changed
                    return existingUser;
                })
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setFullName(fullName);
                    newUser.setRole("ROLE_USER");
                    newUser.setProvider("GOOGLE");
                    return newUser;
                });

        userRepository.save(user); // Save or update user
        return jwtUtil.generateToken(user.getEmail()); // Return JWT
    }
}
