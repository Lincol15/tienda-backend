package com.example.caporalescristos.controller.admin;

import com.example.caporalescristos.dto.ProductoDto;
import com.example.caporalescristos.dto.ProductoRequest;
import com.example.caporalescristos.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

/**
 * Panel admin: gestión de productos (tienda C'Origen).
 * Requiere JWT con rol ADMIN.
 */
@RestController
@RequestMapping("/api/admin/productos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoDto>> listar() {
        return ResponseEntity.ok(productoService.listarTodosAdmin());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorIdAdmin(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoDto> crear(
            @RequestPart(value = "datos", required = false) ProductoRequest request,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) BigDecimal precio,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Integer stock,
            @RequestParam(required = false) Boolean activo
    ) {
        ProductoRequest body = request;
        if (body == null) {
            body = new ProductoRequest();
            body.setNombre(nombre);
            body.setDescripcion(descripcion);
            body.setPrecio(precio);
            body.setCategoriaId(categoriaId);
            body.setStock(stock != null ? stock : 0);
            body.setActivo(activo != null ? activo : true);
        }
        ProductoDto dto = productoService.crear(body, imagen);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoDto> actualizar(
            @PathVariable Long id,
            @RequestPart(value = "datos", required = false) ProductoRequest request,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen
    ) {
        return ResponseEntity.ok(productoService.actualizar(id, request, imagen));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
