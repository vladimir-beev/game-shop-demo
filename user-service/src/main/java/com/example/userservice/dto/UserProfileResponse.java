package com.example.userservice.dto;

public record UserProfileResponse(
        String id,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        String avatarUrl
) {}
