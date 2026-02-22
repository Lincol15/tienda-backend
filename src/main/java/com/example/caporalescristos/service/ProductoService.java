package com.example.caporalescristos.service;

import com.example.caporalescristos.dto.ProductoDto;
import com.example.caporalescristos.dto.ProductoRequest;
import com.example.caporalescristos.entity.Categoria;
import com.example.caporalescristos.entity.Producto;
import com.example.caporalescristos.exception.ResourceNotFoundException;
import com.example.caporalescristos.repository.CategoriaRepository;
import com.example.caporalescristos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private static final String UPLOAD_SUBFOLDER = "productos";

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public List<ProductoDto> listarPublicos() {
        return productoRepository.findByActivoTrueOrderByFechaCreacionDesc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductoDto> listarPublicosPorCategoria(Long categoriaId) {
        return productoRepository.findByActivoTrueAndCategoriaIdOrderByFechaCreacionDesc(categoriaId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductoDto obtenerPorId(Long id) {
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        if (!p.getActivo()) {
            throw new ResourceNotFoundException("Producto", id);
        }
        return toDto(p);
    }

    @Transactional(readOnly = true)
    public List<ProductoDto> listarTodosAdmin() {
        return productoRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductoDto obtenerPorIdAdmin(Long id) {
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        return toDto(p);
    }

    @Transactional
    public ProductoDto crear(ProductoRequest request, MultipartFile imagen) {
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new IllegalArgumentException("Nombre del producto es obligatorio");
        }
        if (request.getPrecio() == null || request.getPrecio().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Precio es obligatorio y debe ser mayor o igual a 0");
        }
        String urlImagen = null;
        if (imagen != null && !imagen.isEmpty()) {
            urlImagen = fileStorageService.storeFile(imagen, UPLOAD_SUBFOLDER);
        }
        Categoria categoria = null;
        if (request.getCategoriaId() != null) {
            categoria = categoriaRepository.findById(request.getCategoriaId())
                    .orElse(null);
        }
        Producto producto = Producto.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precio(request.getPrecio())
                .urlImagen(urlImagen)
                .categoria(categoria)
                .stock(request.getStock() != null ? request.getStock() : 0)
                .activo(request.getActivo() != null ? request.getActivo() : true)
                .build();
        producto = productoRepository.save(producto);
        return toDto(producto);
    }

    @Transactional
    public ProductoDto actualizar(Long id, ProductoRequest request, MultipartFile imagenNueva) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        if (request.getNombre() != null) producto.setNombre(request.getNombre());
        if (request.getDescripcion() != null) producto.setDescripcion(request.getDescripcion());
        if (request.getPrecio() != null) producto.setPrecio(request.getPrecio());
        if (request.getStock() != null) producto.setStock(request.getStock());
        if (request.getActivo() != null) producto.setActivo(request.getActivo());
        if (request.getCategoriaId() != null) {
            Categoria cat = categoriaRepository.findById(request.getCategoriaId()).orElse(null);
            producto.setCategoria(cat);
        }
        if (imagenNueva != null && !imagenNueva.isEmpty()) {
            if (producto.getUrlImagen() != null) {
                fileStorageService.deleteFile(producto.getUrlImagen());
            }
            producto.setUrlImagen(fileStorageService.storeFile(imagenNueva, UPLOAD_SUBFOLDER));
        }
        producto = productoRepository.save(producto);
        return toDto(producto);
    }

    @Transactional
    public void eliminar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        if (producto.getUrlImagen() != null) {
            fileStorageService.deleteFile(producto.getUrlImagen());
        }
        productoRepository.delete(producto);
    }

    private ProductoDto toDto(Producto p) {
        return ProductoDto.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .descripcion(p.getDescripcion())
                .precio(p.getPrecio())
                .urlImagen(p.getUrlImagen())
                .categoriaId(p.getCategoria() != null ? p.getCategoria().getId() : null)
                .categoriaNombre(p.getCategoria() != null ? p.getCategoria().getNombre() : null)
                .stock(p.getStock() != null ? p.getStock() : 0)
                .activo(p.getActivo())
                .fechaCreacion(p.getFechaCreacion())
                .build();
    }
}
