package com.example.caporalescristos.controller;

import com.example.caporalescristos.dto.FotoDto;
import com.example.caporalescristos.service.FotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    /** Sirve la imagen desde BD (Caporales Cristos – persiste en Render). */
    @GetMapping("/{id}/imagen")
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable Long id) {
        return fotoService.getFotoParaImagen(id)
                .map(f -> {
                    MediaType mediaType = MediaType.parseMediaType(
                            f.getContentTypeImagen() != null ? f.getContentTypeImagen() : "image/jpeg");
                    return ResponseEntity.ok()
                            .contentType(mediaType)
                            .header(HttpHeaders.CACHE_CONTROL, "max-age=86400")
                            .body(f.getImagenData());
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
