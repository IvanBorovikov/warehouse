package com.warehouse.warehouse_backend.repository;

import com.warehouse.warehouse_backend.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsSupplierByEmail(String email);
}
