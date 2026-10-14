package com.compramx.backendcompramx.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.compramx.backendcompramx.model.SellerApplication;
import com.compramx.backendcompramx.model.User;
import com.compramx.backendcompramx.repository.SellerApplicationRepository;
import com.compramx.backendcompramx.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.compramx.backendcompramx.dto.SellerApplicationDTO;
import java.util.List;
import java.time.LocalDateTime;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerApplicationRepository sellerApplicationRepository;
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;

    public void apply(String email, String curp, MultipartFile ineFront, MultipartFile ineBack) throws IOException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (sellerApplicationRepository.existsByUserIdAndStatus(user.getId(), "PENDIENTE")) {
            throw new RuntimeException("Ya tienes una solicitud pendiente");
        }

        String frontUrl = uploadImage(ineFront, "ine_front_" + user.getId());
        String backUrl  = uploadImage(ineBack,  "ine_back_"  + user.getId());

        SellerApplication application = new SellerApplication();
        application.setUser(user);
        application.setCurp(curp.toUpperCase());
        application.setIneFrontUrl(frontUrl);
        application.setIneBackUrl(backUrl);

        sellerApplicationRepository.save(application);
    }

    private String uploadImage(MultipartFile file, String publicId) throws IOException {
        Map result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "public_id", "compramx/ine/" + publicId,
                        "format",    "webp",
                        "overwrite", true
                )
        );
        return (String) result.get("secure_url");
    }
    public String getStatus(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return sellerApplicationRepository
                .findTopByUserIdOrderByCreatedAtDesc(user.getId())
                .map(SellerApplication::getStatus)
                .orElse("SIN_SOLICITUD");
    }
    @Transactional
    public List<SellerApplicationDTO> getAllApplications() {
        return sellerApplicationRepository.findAll()
                .stream()
                .map(app -> new SellerApplicationDTO(
                        app.getId(),
                        app.getUser().getId(),
                        app.getUser().getFullName(),
                        app.getUser().getEmail(),
                        app.getCurp(),
                        app.getIneFrontUrl(),
                        app.getIneBackUrl(),
                        app.getStatus(),
                        app.getCreatedAt()
                ))
                .toList();
    }
    @Transactional
    public void approveApplication(Long id) {
        SellerApplication app = sellerApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        app.setStatus("APROBADO");
        app.setUpdatedAt(LocalDateTime.now());
        sellerApplicationRepository.save(app);

        User user = app.getUser();
        user.setRole("VENDEDOR");
        user.setIsSeller(true);
        user.setSellerStatus("APROBADO");
        userRepository.save(user);
    }
    @Transactional
    public void rejectApplication(Long id) {
        SellerApplication app = sellerApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        app.setStatus("RECHAZADO");
        app.setUpdatedAt(LocalDateTime.now());
        sellerApplicationRepository.save(app);

        User user = app.getUser();
        user.setSellerStatus("RECHAZADO");
        userRepository.save(user);
    }
}