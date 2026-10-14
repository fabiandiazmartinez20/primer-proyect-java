package com.compramx.backendcompramx.controller;

import com.compramx.backendcompramx.dto.SellerApplicationDTO;
import com.compramx.backendcompramx.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @PostMapping(value = "/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> apply(
            Principal principal,
            @RequestParam("curp")      String curp,
            @RequestParam("ineFront")  MultipartFile ineFront,
            @RequestParam("ineBack")   MultipartFile ineBack
    ) {
        try {
            sellerService.apply(principal.getName(), curp, ineFront, ineBack);
            return ResponseEntity.ok("Solicitud enviada correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/status")
    public ResponseEntity<?> getStatus(Principal principal) {
        String status = sellerService.getStatus(principal.getName());
        return ResponseEntity.ok(Map.of("status", status));
    }

    @GetMapping("/admin/solicitudes")
    public ResponseEntity<List<SellerApplicationDTO>> getAllApplications() {
        return ResponseEntity.ok(sellerService.getAllApplications());
    }

    @PutMapping("/admin/solicitudes/{id}/aprobar")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        try {
            sellerService.approveApplication(id);
            return ResponseEntity.ok("Solicitud aprobada");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/admin/solicitudes/{id}/rechazar")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        try {
            sellerService.rejectApplication(id);
            return ResponseEntity.ok("Solicitud rechazada");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}