package com.example.caporalescristos.controller.admin;

import com.example.caporalescristos.dto.MetodoPagoDto;
import com.example.caporalescristos.dto.MetodoPagoRequest;
import com.example.caporalescristos.service.MetodoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Panel admin: CRUD de métodos de pago (Yape, Plin, etc.).
 */
@RestController
@RequestMapping("/api/admin/metodos-pago")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminMetodoPagoController {

    private final MetodoPagoService metodoPagoService;

    @GetMapping
    public ResponseEntity<List<MetodoPagoDto>> listar() {
        return ResponseEntity.ok(metodoPagoService.listarTodosAdmin());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetodoPagoDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(metodoPagoService.obtenerPorIdAdmin(id));
    }

    @PostMapping
    public ResponseEntity<MetodoPagoDto> crear(@RequestBody MetodoPagoRequest request) {
        MetodoPagoDto dto = metodoPagoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetodoPagoDto> actualizar(@PathVariable Long id, @RequestBody MetodoPagoRequest request) {
        return ResponseEntity.ok(metodoPagoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        metodoPagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
