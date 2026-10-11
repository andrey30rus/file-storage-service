package com.storage.filestorageservice.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 5, max = 20, message = "Username must be between 8 and 50 characters")
        String username,
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters") String password
) {

}
