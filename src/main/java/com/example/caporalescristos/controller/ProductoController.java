package com.example.caporalescristos.controller;

import com.example.caporalescristos.dto.ProductoDto;
import com.example.caporalescristos.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API pública: tienda C'Origen - productos visibles.
 * Solo lectura para visitantes.
 */
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoDto>> listar(
            @RequestParam(required = false) Long categoriaId
    ) {
        if (categoriaId != null) {
            return ResponseEntity.ok(productoService.listarPublicosPorCategoria(categoriaId));
        }
        return ResponseEntity.ok(productoService.listarPublicos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }
}
