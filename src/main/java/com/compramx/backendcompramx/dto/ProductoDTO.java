package com.compramx.backendcompramx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String titulo;
    private BigDecimal precio;
    private Integer stock;
    private String unidad;
    private String categoria;
    private Boolean isActive;
    private Boolean isFeatured;
    private Integer vistas;
    private Integer ventas;
    private String imagenPrincipal;
    private LocalDateTime createdAt;
}