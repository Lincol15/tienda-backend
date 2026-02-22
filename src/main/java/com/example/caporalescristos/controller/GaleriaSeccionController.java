package com.example.caporalescristos.controller;

import com.example.caporalescristos.dto.GaleriaSeccionDto;
import com.example.caporalescristos.service.GaleriaSeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API pública: secciones de galería con sus fotos (títulos como "Cultural / Tradicional", "Trajes damas").
 * Cada sección incluye la lista de fotos activas.
 */
@RestController
@RequestMapping("/api/secciones-galeria")
@RequiredArgsConstructor
public class GaleriaSeccionController {

    private final GaleriaSeccionService galeriaSeccionService;

    @GetMapping
    public ResponseEntity<List<GaleriaSeccionDto>> listar() {
        return ResponseEntity.ok(galeriaSeccionService.listarPublicasConFotos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GaleriaSeccionDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(galeriaSeccionService.obtenerPorIdPublico(id));
    }
}
