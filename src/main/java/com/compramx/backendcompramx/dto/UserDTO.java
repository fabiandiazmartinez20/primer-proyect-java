package com.compramx.backendcompramx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String fullName;
    private String email;
    private String role;
    private Boolean isSeller;
    private String sellerStatus;
    private LocalDateTime createdAt;
}