package com.compramx.backendcompramx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ProductoDetalleDTO {
    private Long id;
    private String nombre;
    private String titulo;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String unidad;
    private String categoria;
    private Boolean isActive;
    private Boolean isFeatured;
    private Integer vistas;
    private Integer ventas;
    private List<String> imagenes;
    private String vendedor;
    private LocalDateTime createdAt;
}