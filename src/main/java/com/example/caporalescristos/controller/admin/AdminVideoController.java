package com.example.caporalescristos.controller.admin;

import com.example.caporalescristos.dto.VideoDto;
import com.example.caporalescristos.dto.VideoRequest;
import com.example.caporalescristos.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Panel admin: gestión de videos (Cristos Caporales).
 * Crear/actualizar: multipart con part "datos" (JSON) y part "video" (archivo opcional).
 * Si se sube archivo, se usa ese; si no, "datos" debe incluir urlVideo.
 */
@RestController
@RequestMapping("/api/admin/videos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminVideoController {

    private final VideoService videoService;

    @GetMapping
    public ResponseEntity<List<VideoDto>> listar() {
        return ResponseEntity.ok(videoService.listarTodosAdmin());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(videoService.obtenerPorIdAdmin(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VideoDto> crear(
            @RequestPart("datos") VideoRequest request,
            @RequestPart(value = "video", required = false) MultipartFile video
    ) {
        VideoDto dto = videoService.crear(request, video);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VideoDto> actualizar(
            @PathVariable Long id,
            @RequestPart("datos") VideoRequest request,
            @RequestPart(value = "video", required = false) MultipartFile video
    ) {
        return ResponseEntity.ok(videoService.actualizar(id, request, video));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        videoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
