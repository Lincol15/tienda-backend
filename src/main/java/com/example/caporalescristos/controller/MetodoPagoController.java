package com.example.caporalescristos.controller;

import com.example.caporalescristos.dto.MetodoPagoDto;
import com.example.caporalescristos.service.MetodoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API pública: métodos de pago activos (Yape, etc.) para mostrar en la tienda.
 */
@RestController
@RequestMapping("/api/metodos-pago")
@RequiredArgsConstructor
public class MetodoPagoController {

    private final MetodoPagoService metodoPagoService;

    @GetMapping
    public ResponseEntity<List<MetodoPagoDto>> listar() {
        return ResponseEntity.ok(metodoPagoService.listarActivos());
    }
}
