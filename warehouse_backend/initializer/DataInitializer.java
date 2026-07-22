package com.warehouse.warehouse_backend.initializer;

import com.warehouse.warehouse_backend.model.User;
import com.warehouse.warehouse_backend.repository.UserRepository;
import com.warehouse.warehouse_backend.role.Role;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init(){
        if (!repository.existsByEmail("admin@warehouse.com")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@warehouse.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFull_name("System Administrator");
            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            admin.setCreated_at(LocalDateTime.now());
            repository.save(admin);
            System.out.println("Admin created: admin@warehouse.com / admin123");
        } else {
            System.out.println("Admin already");
        }

    }


}
