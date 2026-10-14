package com.compramx.backendcompramx.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "seller_applications")
public class SellerApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 18)
    private String curp;

    @Column(name = "ine_front_url", nullable = false, length = 500)
    private String ineFrontUrl;

    @Column(name = "ine_back_url", nullable = false, length = 500)
    private String ineBackUrl;

    @Column(nullable = false)
    private String status = "PENDIENTE";

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}