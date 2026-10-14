package com.compramx.backendcompramx.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductoRequest {
    private String nombre;
    private String titulo;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private Long categoriaId;
    private String unidad;
    private Boolean isActive;
    private Boolean isFeatured;
    private List<String> imagenesUrls;
}