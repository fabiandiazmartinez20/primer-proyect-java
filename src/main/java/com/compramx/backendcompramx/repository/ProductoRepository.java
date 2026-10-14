package com.compramx.backendcompramx.repository;

import com.compramx.backendcompramx.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    long countByIsActiveTrue();
    List<Producto> findByVendedorId(Long vendedorId);
    List<Producto> findByIsActiveTrueOrderByCreatedAtDesc();
    List<Producto> findByIsActiveTrueAndCategoriaSlugOrderByCreatedAtDesc(String slug);
}