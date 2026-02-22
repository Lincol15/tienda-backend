package com.example.caporalescristos.controller;

import com.example.caporalescristos.dto.VideoDto;
import com.example.caporalescristos.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API pública: Cristos Caporales - videos.
 * Solo lectura para visitantes.
 */
@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping
    public ResponseEntity<List<VideoDto>> listar() {
        return ResponseEntity.ok(videoService.listarPublicos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(videoService.obtenerPorId(id));
    }
}
