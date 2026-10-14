package com.compramx.backendcompramx.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.compramx.backendcompramx.dto.ProductoDTO;
import com.compramx.backendcompramx.dto.ProductoDetalleDTO;
import com.compramx.backendcompramx.dto.ProductoRequest;
import com.compramx.backendcompramx.model.*;
import com.compramx.backendcompramx.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoImagenRepository productoImagenRepository;
    private final UserRepository userRepository;
    private final CategoriaRepository categoriaRepository;
    private final Cloudinary cloudinary;

    @Transactional
    public void crearProducto(String email, ProductoRequest request) {

        User vendedor = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Producto producto = new Producto();
        producto.setVendedor(vendedor);
        producto.setCategoria(categoria);
        producto.setNombre(request.getNombre());
        producto.setTitulo(request.getTitulo());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setUnidad(request.getUnidad());
        producto.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        producto.setIsFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false);

        productoRepository.save(producto);

        List<String> urls = request.getImagenesUrls();
        for (int i = 0; i < urls.size(); i++) {
            ProductoImagen imagen = new ProductoImagen();
            imagen.setProducto(producto);
            imagen.setUrl(urls.get(i));
            imagen.setEsPrincipal(i == 0);
            imagen.setOrden(i);
            productoImagenRepository.save(imagen);
        }
    }

    private String uploadImagen(MultipartFile file, String publicId) throws IOException {
        Map result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "public_id", publicId,
                        "format", "webp",
                        "overwrite", true
                )
        );
        return (String) result.get("secure_url");
    }
    @Transactional
    public List<ProductoDTO> getProductosByVendedor(String email) {
        User vendedor = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return productoRepository.findByVendedorId(vendedor.getId())
                .stream()
                .map(p -> new ProductoDTO(
                        p.getId(),
                        p.getNombre(),
                        p.getTitulo(),
                        p.getPrecio(),
                        p.getStock(),
                        p.getUnidad(),
                        p.getCategoria().getNombre(),
                        p.getIsActive(),
                        p.getIsFeatured(),
                        p.getVistas(),
                        p.getVentas(),
                        p.getImagenes().stream()
                                .filter(ProductoImagen::getEsPrincipal)
                                .map(ProductoImagen::getUrl)
                                .findFirst()
                                .orElse(null),
                        p.getCreatedAt()
                ))
                .toList();
    }
    @Transactional
    public void eliminarProducto(Long id, String email) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (!producto.getVendedor().getEmail().equals(email)) {
            throw new RuntimeException("No tienes permiso para eliminar este producto");
        }

        productoRepository.delete(producto);
    }
    @Transactional
    public List<ProductoDTO> getProductosPublicos() {
        return productoRepository.findByIsActiveTrueOrderByCreatedAtDesc()
                .stream()
                .map(p -> new ProductoDTO(
                        p.getId(),
                        p.getNombre(),
                        p.getTitulo(),
                        p.getPrecio(),
                        p.getStock(),
                        p.getUnidad(),
                        p.getCategoria().getNombre(),
                        p.getIsActive(),
                        p.getIsFeatured(),
                        p.getVistas(),
                        p.getVentas(),
                        p.getImagenes().stream()
                                .filter(img -> Boolean.TRUE.equals(img.getEsPrincipal()))
                                .map(img -> img.getUrl())
                                .findFirst()
                                .orElse(null),
                        p.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
    @Transactional
    public ProductoDetalleDTO getProductoDetalle(Long id) {
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        List<String> imagenes = p.getImagenes().stream()
                .sorted(Comparator.comparingInt(ProductoImagen::getOrden))
                .map(ProductoImagen::getUrl)
                .collect(Collectors.toList());

        return new ProductoDetalleDTO(
                p.getId(),
                p.getNombre(),
                p.getTitulo(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getStock(),
                p.getUnidad(),
                p.getCategoria().getNombre(),
                p.getIsActive(),
                p.getIsFeatured(),
                p.getVistas(),
                p.getVentas(),
                imagenes,
                p.getVendedor().getFullName(),
                p.getCreatedAt()
        );
    }
    @Transactional
    public List<ProductoDTO> getProductosPorCategoria(String slug) {
        return productoRepository.findByIsActiveTrueAndCategoriaSlugOrderByCreatedAtDesc(slug)
                .stream()
                .map(p -> new ProductoDTO(
                        p.getId(),
                        p.getNombre(),
                        p.getTitulo(),
                        p.getPrecio(),
                        p.getStock(),
                        p.getUnidad(),
                        p.getCategoria().getNombre(),
                        p.getIsActive(),
                        p.getIsFeatured(),
                        p.getVistas(),
                        p.getVentas(),
                        p.getImagenes().stream()
                                .filter(img -> Boolean.TRUE.equals(img.getEsPrincipal()))
                                .map(ProductoImagen::getUrl)
                                .findFirst()
                                .orElse(null),
                        p.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}