package com.compramx.backendcompramx.service;

import com.compramx.backendcompramx.dto.UserDTO;
import com.compramx.backendcompramx.model.Producto;
import com.compramx.backendcompramx.repository.ProductoRepository;
import com.compramx.backendcompramx.repository.SellerApplicationRepository;
import com.compramx.backendcompramx.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SellerApplicationRepository sellerApplicationRepository;
    private final ProductoRepository productoRepository;

    public List<UserDTO> searchUsers(String query) {
        return userRepository.searchByNameOrEmail(query.trim())
                .stream()
                .map(u -> new UserDTO(
                        u.getId(),
                        u.getFullName(),
                        u.getEmail(),
                        u.getRole(),
                        u.getIsSeller(),
                        u.getSellerStatus(),
                        u.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // 1. Borrar seller_applications del usuario
        sellerApplicationRepository.deleteByUserId(id);

        // 2. Borrar productos del usuario (ProductoImagen se borra en cascada)
        List<Producto> productos = productoRepository.findByVendedorId(id);
        productoRepository.deleteAll(productos);

        // 3. Borrar el usuario
        userRepository.deleteById(id);
    }
}