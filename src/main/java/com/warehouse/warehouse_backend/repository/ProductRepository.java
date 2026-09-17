package com.warehouse.warehouse_backend.repository;

import com.warehouse.warehouse_backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findProductByArticle(String article, Pageable pageable);
    // ✅ Частичный поиск (регистронезависимый)
    Page<Product> findByArticleContainingIgnoreCase(String article, Pageable pageable);

    boolean existsByArticle(String article);
}
