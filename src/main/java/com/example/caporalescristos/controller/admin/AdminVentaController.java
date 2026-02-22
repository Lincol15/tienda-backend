package com.example.caporalescristos.controller.admin;

import com.example.caporalescristos.dto.VentaDto;
import com.example.caporalescristos.dto.VentaRequest;
import com.example.caporalescristos.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Panel admin: listar ventas/pedidos, crear venta manual, actualizar estado.
 */
@RestController
@RequestMapping("/api/admin/ventas")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminVentaController {

    private final VentaService ventaService;

    @GetMapping
    public ResponseEntity<List<VentaDto>> listar() {
        return ResponseEntity.ok(ventaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<VentaDto> crear(@RequestBody VentaRequest request) {
        VentaDto dto = ventaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<VentaDto> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String estado = body != null ? body.get("estado") : null;
        return ResponseEntity.ok(ventaService.actualizarEstado(id, estado));
    }
}
