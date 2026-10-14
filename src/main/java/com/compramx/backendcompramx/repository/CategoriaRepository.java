package com.compramx.backendcompramx.repository;

import com.compramx.backendcompramx.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByIsActiveTrue();
}