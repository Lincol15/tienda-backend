package com.example.caporalescristos.controller.admin;

import com.example.caporalescristos.dto.ConfiguracionInicioDto;
import com.example.caporalescristos.service.ConfiguracionInicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Panel admin: actualizar imagen de portada (banner) y/o logo de la página de inicio.
 * Multipart: part "portada" (opcional), part "logo" (opcional). Al menos una obligatoria.
 * consumes = MediaType.ALL_VALUE evita que Spring rechace la petición cuando el cliente
 * no envía exactamente multipart/form-data (p. ej. boundary incorrecto), así el controlador
 * hace match y el servicio puede devolver 400 si faltan las partes.
 */
@RestController
@RequestMapping("/api/admin/configuracion-inicio")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminConfiguracionInicioController {

    private final ConfiguracionInicioService configuracionInicioService;

    @PutMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity<ConfiguracionInicioDto> actualizar(
            @RequestPart(value = "portada", required = false) MultipartFile portada,
            @RequestPart(value = "logo", required = false) MultipartFile logo) {
        ConfiguracionInicioDto dto = configuracionInicioService.actualizar(portada, logo);
        return ResponseEntity.ok(dto);
    }
}
