package com.virtual_try_backend.shoppingApp.controller;

import com.virtual_try_backend.shoppingApp.dto.LoginRequest;
import com.virtual_try_backend.shoppingApp.dto.SignupRequest;
import com.virtual_try_backend.shoppingApp.service.UserService;
import com.virtual_try_backend.shoppingApp.config.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // ✅ User Signup
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody SignupRequest request) {
        String message = userService.registerUser(request);
        return ResponseEntity.ok(Map.of("message", message));
    }

    // ✅ User Login with JWT
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        String token = userService.loginUser(request);
        return ResponseEntity.ok(Map.of("token", token));
    }

    // ✅ Google OAuth2 Login Success Handler
    @GetMapping("/google-success")
    public ResponseEntity<Map<String, String>> googleSuccess(@AuthenticationPrincipal OAuth2User user) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        String email = user.getAttribute("email");
        String token = jwtUtil.generateToken(email);

        return ResponseEntity.ok(Map.of("token", token, "email", email));
    }
}
