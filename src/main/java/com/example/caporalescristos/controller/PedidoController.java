package com.example.caporalescristos.controller;

import com.example.caporalescristos.dto.VentaDto;
import com.example.caporalescristos.dto.VentaRequest;
import com.example.caporalescristos.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API pública: registrar pedido/venta cuando el cliente confirma el carrito (opcional).
 * Crea la venta con estado PENDIENTE para que el admin la vea y gestione.
 */
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final VentaService ventaService;

    @PostMapping
    public ResponseEntity<VentaDto> registrar(@RequestBody VentaRequest request) {
        if (request.getEstado() == null || request.getEstado().isBlank()) {
            request.setEstado("PENDIENTE");
        }
        VentaDto dto = ventaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
