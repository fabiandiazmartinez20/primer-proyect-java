package com.compramx.backendcompramx.controller;

import com.compramx.backendcompramx.dto.ProductoRequest;
import com.compramx.backendcompramx.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<?> crearProducto(
            Principal principal,
            @RequestBody ProductoRequest request
    ) {
        try {
            productoService.crearProducto(principal.getName(), request);
            return ResponseEntity.ok("Producto publicado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/mis-productos")
    public ResponseEntity<?> getMisProductos(Principal principal) {
        try {
            List<?> productos = productoService.getProductosByVendedor(principal.getName());
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/publicos")
    public ResponseEntity<?> getProductosPublicos() {
        return ResponseEntity.ok(productoService.getProductosPublicos());
    }

    @GetMapping("/categoria/{slug}")
    public ResponseEntity<?> getProductosPorCategoria(@PathVariable String slug) {
        return ResponseEntity.ok(productoService.getProductosPorCategoria(slug));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(
            @PathVariable Long id,
            Principal principal
    ) {
        try {
            productoService.eliminarProducto(id, principal.getName());
            return ResponseEntity.ok("Producto eliminado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // /{id} siempre al final
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductoDetalle(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productoService.getProductoDetalle(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}