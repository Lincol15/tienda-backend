package com.example.caporalescristos.service;

import com.example.caporalescristos.dto.ConfiguracionInicioDto;
import com.example.caporalescristos.entity.ConfiguracionInicio;
import com.example.caporalescristos.repository.ConfiguracionInicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfiguracionInicioService {

    private static final String CLOUDINARY_FOLDER_PORTADA = "caporales/portada";
    private static final String CLOUDINARY_FOLDER_LOGO = "caporales/logo";
    private static final List<String> ALLOWED_IMAGE_TYPES = List.of(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );

    private final ConfiguracionInicioRepository configuracionInicioRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional(readOnly = true)
    public ConfiguracionInicioDto obtener() {
        ConfiguracionInicio config = configuracionInicioRepository.findFirstByOrderByIdAsc()
                .orElse(null);
        if (config == null) {
            return ConfiguracionInicioDto.builder()
                    .portadaUrl(null)
                    .logoUrl(null)
                    .build();
        }
        return ConfiguracionInicioDto.builder()
                .portadaUrl(config.getPortadaUrl())
                .logoUrl(config.getLogoUrl())
                .build();
    }

    @Transactional
    public ConfiguracionInicioDto actualizar(MultipartFile portada, MultipartFile logo) {
        boolean hasPortada = portada != null && !portada.isEmpty();
        boolean hasLogo = logo != null && !logo.isEmpty();
        if (!hasPortada && !hasLogo) {
            throw new IllegalArgumentException("Debe enviar al menos una imagen (portada o logo)");
        }

        ConfiguracionInicio config = configuracionInicioRepository.findFirstByOrderByIdAsc()
                .orElseGet(() -> {
                    ConfiguracionInicio newConfig = ConfiguracionInicio.builder().build();
                    return configuracionInicioRepository.save(newConfig);
                });

        if (hasPortada) {
            String contentType = portada.getContentType();
            if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
                throw new IllegalArgumentException("La portada debe ser una imagen (JPEG, PNG, GIF, WebP)");
            }
            String newUrl = cloudinaryService.uploadImage(portada, CLOUDINARY_FOLDER_PORTADA);
            if (newUrl != null) {
                config.setPortadaUrl(newUrl);
            }
        }

        if (hasLogo) {
            String contentType = logo.getContentType();
            if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
                throw new IllegalArgumentException("El logo debe ser una imagen (JPEG, PNG, GIF, WebP)");
            }
            String newUrl = cloudinaryService.uploadImage(logo, CLOUDINARY_FOLDER_LOGO);
            if (newUrl != null) {
                config.setLogoUrl(newUrl);
            }
        }

        config = configuracionInicioRepository.save(config);
        return ConfiguracionInicioDto.builder()
                .portadaUrl(config.getPortadaUrl())
                .logoUrl(config.getLogoUrl())
                .build();
    }
}
