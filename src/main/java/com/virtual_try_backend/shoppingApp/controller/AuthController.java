package com.virtual_try_backend.shoppingApp.controller;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.virtual_try_backend.shoppingApp.dto.LoginRequest;
import com.virtual_try_backend.shoppingApp.dto.SignupRequest;
import com.virtual_try_backend.shoppingApp.service.ForgotPasswordService;
import com.virtual_try_backend.shoppingApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    private final UserService userService;

    private static final String CLIENT_ID = "1043781877337-dud8r8vup168a23cobfun80qovghoha2.apps.googleusercontent.com";

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody SignupRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            String result = userService.registerUser(request);
            response.put("message", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            String token = userService.loginUser(request);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/google-signup")
    public ResponseEntity<Map<String, String>> googleSignup(@RequestBody Map<String, String> requestBody) {
        Map<String, String> responseBody = new HashMap<>();
        String idTokenString = requestBody.get("token");

        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                String token = userService.registerOrUpdateOAuth2User(email, name);
                responseBody.put("token", token);
                return ResponseEntity.ok(responseBody);
            } else {
                responseBody.put("message", "Invalid ID token.");
                return ResponseEntity.badRequest().body(responseBody);
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseBody.put("message", "Google login failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(responseBody);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, String> response = new HashMap<>();

        if (email == null || email.trim().isEmpty()) {
            response.put("message", "Email is required");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            forgotPasswordService.sendResetLink(email);
            response.put("message", "Reset link sent to your email");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Failed to send reset link: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        Map<String, String> response = new HashMap<>();

        if (token == null || token.trim().isEmpty()) {
            response.put("message", "Reset token is required");
            return ResponseEntity.badRequest().body(response);
        }

        if (newPassword == null || newPassword.length() < 6) {
            response.put("message", "Password must be at least 6 characters");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            forgotPasswordService.resetPassword(token, newPassword);
            response.put("message", "Password reset successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Password reset failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
