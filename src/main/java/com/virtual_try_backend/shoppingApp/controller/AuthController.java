package com.virtual_try_backend.shoppingApp.controller;

import com.virtual_try_backend.shoppingApp.dto.LoginRequest;
import com.virtual_try_backend.shoppingApp.dto.SignupRequest;
import com.virtual_try_backend.shoppingApp.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private static final String CLIENT_ID = "1043781877337-dud8r8vup168a23cobfun80qovghoha2.apps.googleusercontent.com";

    public AuthController(UserService userService) {
        this.userService = userService;
    }

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

    // ✅ Updated: Google OAuth2 Login & Signup
    @PostMapping("/google-signup")
    public ResponseEntity<Map<String, String>> googleSignup(@RequestBody Map<String, String> requestBody) {
        String idTokenString = requestBody.get("token");
        Map<String, String> responseBody = new HashMap<>();

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
}
