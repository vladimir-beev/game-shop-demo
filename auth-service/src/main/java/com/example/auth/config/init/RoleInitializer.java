package com.example.auth.config.init;

import com.example.auth.entity.RoleName;
import com.example.auth.entity.UserRole;
import com.example.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String @NonNull ... args) {
        for (RoleName roleName : RoleName.values()) {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> {
                        UserRole role = new UserRole();
                        role.setName(roleName);
                        return roleRepository.save(role);
                    });
        }
    }
}
