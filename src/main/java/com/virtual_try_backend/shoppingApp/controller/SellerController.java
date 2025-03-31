package com.virtual_try_backend.shoppingApp.controller;

import com.virtual_try_backend.shoppingApp.dto.LoginRequest;
import com.virtual_try_backend.shoppingApp.dto.SignupRequest;
import com.virtual_try_backend.shoppingApp.service.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/seller/api/auth")
public class SellerController {

    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    // Register new seller
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> registerSeller(@RequestBody SignupRequest request) {
        String response = sellerService.registerSeller(request);
        Map<String, String> responseBody = new HashMap<>();
        if (response.startsWith("Error")) {
            responseBody.put("message", response);
            return ResponseEntity.badRequest().body(responseBody);
        }
        responseBody.put("message", "Signup successful");
        return ResponseEntity.ok(responseBody);
    }

    // Login seller
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginSeller(@RequestBody LoginRequest request) {
        String response = sellerService.loginSeller(request);
        Map<String, String> responseBody = new HashMap<>();
        if (response.startsWith("Error")) {
            responseBody.put("message", response);
            return ResponseEntity.badRequest().body(responseBody);
        }
        responseBody.put("token", response);
        return ResponseEntity.ok(responseBody);
    }
}
