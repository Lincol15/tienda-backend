package com.example.caporalescristos.controller.admin;

import com.example.caporalescristos.dto.GaleriaSeccionDto;
import com.example.caporalescristos.dto.GaleriaSeccionRequest;
import com.example.caporalescristos.service.GaleriaSeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Panel admin: gestión de secciones de galería (títulos como "Trajes damas", "Cultural / Tradicional").
 * Body JSON para crear/actualizar.
 */
@RestController
@RequestMapping("/api/admin/secciones-galeria")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminGaleriaSeccionController {

    private final GaleriaSeccionService galeriaSeccionService;

    @GetMapping
    public ResponseEntity<List<GaleriaSeccionDto>> listar() {
        return ResponseEntity.ok(galeriaSeccionService.listarTodasAdmin());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GaleriaSeccionDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(galeriaSeccionService.obtenerPorIdAdmin(id));
    }

    @PostMapping
    public ResponseEntity<GaleriaSeccionDto> crear(@RequestBody GaleriaSeccionRequest request) {
        GaleriaSeccionDto dto = galeriaSeccionService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GaleriaSeccionDto> actualizar(@PathVariable Long id, @RequestBody GaleriaSeccionRequest request) {
        return ResponseEntity.ok(galeriaSeccionService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        galeriaSeccionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
