package com.compramx.backendcompramx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SellerApplicationDTO {
    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String curp;
    private String ineFrontUrl;
    private String ineBackUrl;
    private String status;
    private LocalDateTime createdAt;
}