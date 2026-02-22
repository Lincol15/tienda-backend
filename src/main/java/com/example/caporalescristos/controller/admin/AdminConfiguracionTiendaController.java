package com.example.caporalescristos.controller.admin;

import com.example.caporalescristos.dto.ConfiguracionTiendaDto;
import com.example.caporalescristos.dto.ConfiguracionTiendaRequest;
import com.example.caporalescristos.service.ConfiguracionTiendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Panel admin: configurar número de WhatsApp de la tienda.
 */
@RestController
@RequestMapping("/api/admin/configuracion-tienda")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminConfiguracionTiendaController {

    private final ConfiguracionTiendaService configuracionTiendaService;

    @GetMapping
    public ResponseEntity<ConfiguracionTiendaDto> obtener() {
        return ResponseEntity.ok(configuracionTiendaService.obtener());
    }

    @PutMapping
    public ResponseEntity<ConfiguracionTiendaDto> actualizar(@RequestBody ConfiguracionTiendaRequest request) {
        return ResponseEntity.ok(configuracionTiendaService.actualizar(request));
    }
}
