package com.virtual_try_backend.shoppingApp.controller;

import com.virtual_try_backend.shoppingApp.dto.LoginRequest;
import com.virtual_try_backend.shoppingApp.dto.SignupRequest;
import com.virtual_try_backend.shoppingApp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/auth")  // ✅ Added `/api` to match frontend
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Register new user (Local)
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody SignupRequest request) {
        String response = userService.registerUser(request);

        Map<String, String> responseBody = new HashMap<>();
        if (response.startsWith("Error")) {
            responseBody.put("message", response);
            return ResponseEntity.badRequest().body(responseBody);
        }

        responseBody.put("message", "Signup successful");
        return ResponseEntity.ok(responseBody);
    }

    // ✅ Login user (Local)
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginRequest request) {
        String token = userService.loginUser(request);

        Map<String, String> responseBody = new HashMap<>();
        if (token.startsWith("Error")) {
            responseBody.put("message", token);
            return ResponseEntity.badRequest().body(responseBody);
        }

        responseBody.put("token", token);
        return ResponseEntity.ok(responseBody);
    }

    // ✅ Google OAuth2 Login & Signup
    @PostMapping("/google-signup")
    public ResponseEntity<Map<String, String>> googleSignup(@RequestBody Map<String, Object> googleUserData) {
        String email = (String) googleUserData.get("email");
        String name = (String) googleUserData.get("name");
        String token = userService.registerOrUpdateOAuth2User(email, name);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);

        return ResponseEntity.ok(responseBody);
    }
}
