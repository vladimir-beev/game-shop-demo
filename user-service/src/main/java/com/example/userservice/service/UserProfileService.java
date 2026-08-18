package com.example.userservice.service;

import com.example.userservice.dto.UpdateUserProfileRequest;
import com.example.userservice.dto.UserProfileResponse;

public interface UserProfileService {
    UserProfileResponse getOrCreateProfile(String userId, String email);

    UserProfileResponse updateProfile(String userId, UpdateUserProfileRequest request);
}
