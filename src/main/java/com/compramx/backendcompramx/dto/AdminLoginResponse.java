package com.compramx.backendcompramx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminLoginResponse {
    private Long id;
    private String fullName;
    private String email;
    private String role;
    private String token;
}