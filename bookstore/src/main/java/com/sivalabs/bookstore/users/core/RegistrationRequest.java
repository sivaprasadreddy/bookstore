package com.sivalabs.bookstore.users.core;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Email is required") @Email(message = "Invalid email address") String email,
        @NotBlank(message = "Password is required") @Size(min = 4, message = "Password must be at least 4 characters") String password) {}
