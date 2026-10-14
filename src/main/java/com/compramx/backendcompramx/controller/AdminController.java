package com.compramx.backendcompramx.controller;

import com.compramx.backendcompramx.dto.AdminLoginResponse;
import com.compramx.backendcompramx.dto.AdminStatsDTO;
import com.compramx.backendcompramx.dto.LoginRequest;
import com.compramx.backendcompramx.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(@RequestBody LoginRequest request) {
        AdminLoginResponse response = adminService.login(request);
        return ResponseEntity.ok(response);
    }
    // Agrega este endpoint (protegido, solo ADMINISTRADOR)
    @GetMapping("/stats")
    public ResponseEntity<AdminStatsDTO> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }
}