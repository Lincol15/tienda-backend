package com.example.caporalescristos.service;

import com.example.caporalescristos.dto.CategoriaDto;
import com.example.caporalescristos.dto.CategoriaRequest;
import com.example.caporalescristos.entity.Categoria;
import com.example.caporalescristos.exception.ResourceNotFoundException;
import com.example.caporalescristos.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaDto> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaDto obtenerPorId(Long id) {
        Categoria cat = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", id));
        return toDto(cat);
    }

    @Transactional
    public CategoriaDto crear(CategoriaRequest request) {
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new IllegalArgumentException("Nombre de la categoría es obligatorio");
        }
        Categoria cat = Categoria.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .build();
        cat = categoriaRepository.save(cat);
        return toDto(cat);
    }

    @Transactional
    public CategoriaDto actualizar(Long id, CategoriaRequest request) {
        Categoria cat = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", id));
        if (request.getNombre() != null) cat.setNombre(request.getNombre());
        if (request.getDescripcion() != null) cat.setDescripcion(request.getDescripcion());
        cat = categoriaRepository.save(cat);
        return toDto(cat);
    }

    @Transactional
    public void eliminar(Long id) {
        Categoria cat = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", id));
        categoriaRepository.delete(cat);
    }

    private CategoriaDto toDto(Categoria c) {
        return CategoriaDto.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .descripcion(c.getDescripcion())
                .build();
    }
}
