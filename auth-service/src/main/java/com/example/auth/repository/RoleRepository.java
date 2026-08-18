package com.example.auth.repository;

import com.example.auth.entity.RoleName;
import com.example.auth.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<UserRole, String> {

    Optional<UserRole> findByName(RoleName name);
}