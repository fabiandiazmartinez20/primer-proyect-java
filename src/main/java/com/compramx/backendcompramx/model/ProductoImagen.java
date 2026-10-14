package com.compramx.backendcompramx.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "producto_imagenes")
public class ProductoImagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(name = "es_principal")
    private Boolean esPrincipal = false;

    @Column(name = "orden")
    private Integer orden = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}