package com.example.caporalescristos.controller;

import com.example.caporalescristos.dto.FotoDto;
import com.example.caporalescristos.service.FotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API pública: Cristos Caporales - galería de fotos.
 * Solo lectura para visitantes.
 */
@RestController
@RequestMapping("/api/fotos")
@RequiredArgsConstructor
public class FotoController {

    private final FotoService fotoService;

    @GetMapping
    public ResponseEntity<List<FotoDto>> listar() {
        return ResponseEntity.ok(fotoService.listarPublicas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FotoDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(fotoService.obtenerPorId(id));
    }
}
