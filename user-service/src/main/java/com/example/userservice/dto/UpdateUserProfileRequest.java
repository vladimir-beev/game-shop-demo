package com.example.userservice.dto;

import jakarta.validation.constraints.Size;

public record UpdateUserProfileRequest(
        @Size(max = 50, message = "First name must not be more than 50 characters")
        String firstName,

        @Size(max = 50, message = "Last name must not be more than 50 characters")
        String lastName,

        String phoneNumber,

        @Size(max = 255, message = "Avatar URL must not be more than 255 characters")
        String avatarUrl
) {
}
