package com.warehouse.warehouse_backend.repository;

import com.warehouse.warehouse_backend.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovementRepository extends JpaRepository<Movement, Long> {
    List<Movement> findByProductIdOrderByCreatedAtDesc(Long productId);
}
