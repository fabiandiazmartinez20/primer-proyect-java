package com.compramx.backendcompramx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminStatsDTO {
    private long totalUsuarios;
    private long solicitudesPendientes;
    private long totalProductos;
    private long totalVendedores;
}