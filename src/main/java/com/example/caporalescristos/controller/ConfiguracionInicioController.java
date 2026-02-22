package com.example.caporalescristos.controller;

import com.example.caporalescristos.dto.ConfiguracionInicioDto;
import com.example.caporalescristos.service.ConfiguracionInicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API pública: leer la configuración de la página de inicio (portada y logo).
 */
@RestController
@RequestMapping("/api/configuracion-inicio")
@RequiredArgsConstructor
public class ConfiguracionInicioController {

    private final ConfiguracionInicioService configuracionInicioService;

    @GetMapping
    public ResponseEntity<ConfiguracionInicioDto> obtener() {
        return ResponseEntity.ok(configuracionInicioService.obtener());
    }
}
