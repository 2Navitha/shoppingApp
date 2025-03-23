package com.virtual_try_backend.shoppingApp.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    @NotBlank
    private String fullName;
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}