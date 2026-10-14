package com.compramx.backendcompramx.controller;

import com.compramx.backendcompramx.dto.LoginRequest;
import com.compramx.backendcompramx.dto.LoginResponse;
import com.compramx.backendcompramx.dto.RegisterRequest;
import com.compramx.backendcompramx.model.User;
import com.compramx.backendcompramx.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        return ResponseEntity.ok("Usuario registrado exitosamente: " + user.getEmail());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}