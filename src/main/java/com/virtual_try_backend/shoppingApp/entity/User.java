package com.virtual_try_backend.shoppingApp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "role", nullable = false)
    private String role = "USER";


    private String provider; // "LOCAL" or "GOOGLE"
}