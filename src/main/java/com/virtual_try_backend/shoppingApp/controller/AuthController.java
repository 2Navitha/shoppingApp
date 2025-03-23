package com.virtual_try_backend.shoppingApp.controller;

import com.virtual_try_backend.shoppingApp.dto.LoginRequest;
import com.virtual_try_backend.shoppingApp.dto.SignupRequest;
import com.virtual_try_backend.shoppingApp.service.UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Register new user (Local)
    @PostMapping("/register")
    public String registerUser(@RequestBody SignupRequest request) {
        return userService.registerUser(request);
    }

    // ✅ Login user (Local)
    @PostMapping("/login")
    public String loginUser(@RequestBody LoginRequest request) {
        return userService.loginUser(request);
    }

    // ✅ Google OAuth2 Login & Signup
    @PostMapping("/google-signup")
    public String googleSignup(@RequestBody Map<String, Object> googleUserData) {
        String email = (String) googleUserData.get("email");
        String name = (String) googleUserData.get("name");
        return userService.registerOrUpdateOAuth2User(email, name);
    }
}
