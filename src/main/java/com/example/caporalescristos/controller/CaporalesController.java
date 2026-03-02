package com.example.caporalescristos.controller;

import com.example.caporalescristos.service.CaporaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Subida Caporales: foto y video a Cloudinary, solo URLs en BD.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CaporalesController {

    private final CaporaleService caporaleService;

    @PostMapping(value = "/caporales", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> crear(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) MultipartFile foto,
            @RequestParam(required = false) MultipartFile video
    ) {
        caporaleService.guardar(titulo, descripcion, foto, video);
        return ResponseEntity.ok("Guardado correctamente");
    }
}
