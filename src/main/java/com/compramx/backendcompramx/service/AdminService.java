package com.compramx.backendcompramx.service;

import com.compramx.backendcompramx.dto.AdminLoginResponse;
import com.compramx.backendcompramx.dto.AdminStatsDTO;
import com.compramx.backendcompramx.dto.LoginRequest;
import com.compramx.backendcompramx.model.Admin;
import com.compramx.backendcompramx.model.User;
import com.compramx.backendcompramx.repository.AdminRepository;
import com.compramx.backendcompramx.repository.ProductoRepository;
import com.compramx.backendcompramx.repository.SellerApplicationRepository;
import com.compramx.backendcompramx.repository.UserRepository;
import com.compramx.backendcompramx.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    // Agrega este campo (ya tienes los otros inyectados)
    private final ProductoRepository productoRepository;
    private final SellerApplicationRepository sellerApplicationRepository;

    public AdminLoginResponse login(LoginRequest request) {

        // Primero buscar en tabla admins
        Admin admin = adminRepository.findByEmail(request.getEmail()).orElse(null);

        if (admin != null) {
            if (!passwordEncoder.matches(request.getPassword(), admin.getPasswordHash())) {
                throw new RuntimeException("Credenciales incorrectas");
            }
            String token = jwtUtil.generateToken(admin.getEmail(), "ADMINISTRADOR");
            return new AdminLoginResponse(
                    admin.getId(),
                    admin.getFullName(),
                    admin.getEmail(),
                    "ADMINISTRADOR",
                    token
            );
        }

        // Si no es admin, buscar en tabla users
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        if (!user.getRole().equals("VENDEDOR")) {
            throw new RuntimeException("No tienes permiso para acceder al panel");
        }

        if (!"APROBADO".equals(user.getSellerStatus())) {
            throw new RuntimeException("Tu solicitud aún no ha sido aprobada");
        }

        String token = jwtUtil.generateToken(user.getEmail(), "VENDEDOR");
        return new AdminLoginResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                "VENDEDOR",
                token
        );
    }

    public AdminStatsDTO getStats() {
        long totalUsuarios = userRepository.count();
        long solicitudesPendientes = sellerApplicationRepository.countByStatus("PENDIENTE");
        long totalProductos = productoRepository.countByIsActiveTrue();
        long totalVendedores = userRepository.countByRole("VENDEDOR");

        return new AdminStatsDTO(totalUsuarios, solicitudesPendientes, totalProductos, totalVendedores);
    }
}