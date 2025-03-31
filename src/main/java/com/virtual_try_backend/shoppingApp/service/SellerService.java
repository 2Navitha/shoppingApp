package com.virtual_try_backend.shoppingApp.service;

import com.virtual_try_backend.shoppingApp.config.JwtUtil;
import com.virtual_try_backend.shoppingApp.dto.LoginRequest;
import com.virtual_try_backend.shoppingApp.dto.SignupRequest;
import com.virtual_try_backend.shoppingApp.entity.Seller;
import com.virtual_try_backend.shoppingApp.repository.SellerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public SellerService(SellerRepository sellerRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.sellerRepository = sellerRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Register new seller with validation and transaction
    @Transactional
    public String registerSeller(SignupRequest request) {
        if (sellerRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Error: Email is already in use!";
        }

        // Creating seller account
        Seller seller = new Seller();
        seller.setFullName(request.getFullName());
        seller.setEmail(request.getEmail());
        seller.setPassword(passwordEncoder.encode(request.getPassword())); // ✅ Secure Password Hashing
        seller.setRole("ROLE_SELLER");
        seller.setProvider("LOCAL");

        sellerRepository.save(seller);
        return "Seller registered successfully!";
    }

    // ✅ Login seller with proper error handling
    public String loginSeller(LoginRequest request) {
        Optional<Seller> sellerOpt = sellerRepository.findByEmail(request.getEmail());

        if (sellerOpt.isEmpty()) {
            return "Error: Email not registered!";
        }

        Seller seller = sellerOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), seller.getPassword())) {
            return "Error: Invalid email or password!";
        }

        return jwtUtil.generateToken(seller.getEmail()); // ✅ Secure JWT Token Generation
    }
}
