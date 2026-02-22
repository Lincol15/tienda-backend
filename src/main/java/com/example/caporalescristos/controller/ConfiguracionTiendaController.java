package com.example.caporalescristos.controller;

import com.example.caporalescristos.dto.ConfiguracionTiendaDto;
import com.example.caporalescristos.service.ConfiguracionTiendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API pública: configuración de la tienda (WhatsApp, etc.) para el frontend.
 */
@RestController
@RequestMapping("/api/configuracion-tienda")
@RequiredArgsConstructor
public class ConfiguracionTiendaController {

    private final ConfiguracionTiendaService configuracionTiendaService;

    @GetMapping
    public ResponseEntity<ConfiguracionTiendaDto> obtener() {
        return ResponseEntity.ok(configuracionTiendaService.obtener());
    }
}
