package com.example.caporalescristos.controller.admin;

import com.example.caporalescristos.dto.FotoDto;
import com.example.caporalescristos.dto.FotoRequest;
import com.example.caporalescristos.service.FotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Panel admin: gestión de fotos (Cristos Caporales).
 * Requiere JWT con rol ADMIN.
 */
@RestController
@RequestMapping("/api/admin/fotos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminFotoController {

    private final FotoService fotoService;

    @GetMapping
    public ResponseEntity<List<FotoDto>> listar() {
        return ResponseEntity.ok(fotoService.listarTodasAdmin());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FotoDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(fotoService.obtenerPorIdAdmin(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FotoDto> crear(
            @RequestPart("datos") FotoRequest request,
            @RequestPart("imagen") MultipartFile imagen
    ) {
        FotoDto dto = fotoService.crear(request, imagen);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FotoDto> actualizar(
            @PathVariable Long id,
            @RequestPart("datos") FotoRequest request,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen
    ) {
        return ResponseEntity.ok(fotoService.actualizar(id, request, imagen));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        fotoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
