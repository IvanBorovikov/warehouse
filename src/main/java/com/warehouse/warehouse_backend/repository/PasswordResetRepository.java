package com.warehouse.warehouse_backend.repository;

import com.warehouse.warehouse_backend.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordResetToken, Long> {
    void deleteByEmail(String email);
    Optional<PasswordResetToken> findByToken(String token);

}
