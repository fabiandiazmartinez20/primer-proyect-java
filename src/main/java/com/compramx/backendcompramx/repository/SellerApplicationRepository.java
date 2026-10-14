package com.compramx.backendcompramx.repository;

import com.compramx.backendcompramx.model.SellerApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerApplicationRepository extends JpaRepository<SellerApplication, Long> {
    boolean existsByUserIdAndStatus(Long userId, String status);
    long countByStatus(String status);
    void deleteByUserId(Long userId);

    Optional<SellerApplication> findTopByUserIdOrderByCreatedAtDesc(Long userId);

}