package com.example.caporalescristos.service;

import com.example.caporalescristos.dto.FotoDto;
import com.example.caporalescristos.dto.GaleriaSeccionDto;
import com.example.caporalescristos.dto.GaleriaSeccionRequest;
import com.example.caporalescristos.entity.Foto;
import com.example.caporalescristos.entity.GaleriaSeccion;
import com.example.caporalescristos.exception.ResourceNotFoundException;
import com.example.caporalescristos.repository.FotoRepository;
import com.example.caporalescristos.repository.GaleriaSeccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GaleriaSeccionService {

    private final GaleriaSeccionRepository galeriaSeccionRepository;
    private final FotoRepository fotoRepository;

    @Transactional(readOnly = true)
    public List<GaleriaSeccionDto> listarPublicasConFotos() {
        return galeriaSeccionRepository.findByActivoTrueOrderByOrdenAsc().stream()
                .map(this::toDtoConFotos)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GaleriaSeccionDto obtenerPorIdPublico(Long id) {
        GaleriaSeccion seccion = galeriaSeccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GaleriaSeccion", id));
        if (!seccion.getActivo()) {
            throw new ResourceNotFoundException("GaleriaSeccion", id);
        }
        return toDtoConFotos(seccion);
    }

    @Transactional(readOnly = true)
    public List<GaleriaSeccionDto> listarTodasAdmin() {
        return galeriaSeccionRepository.findAllByOrderByOrdenAsc().stream()
                .map(this::toDtoSinFotos)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GaleriaSeccionDto obtenerPorIdAdmin(Long id) {
        GaleriaSeccion seccion = galeriaSeccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GaleriaSeccion", id));
        return toDtoConFotos(seccion);
    }

    @Transactional
    public GaleriaSeccionDto crear(GaleriaSeccionRequest request) {
        GaleriaSeccion seccion = GaleriaSeccion.builder()
                .titulo(request.getTitulo())
                .descripcion(request.getDescripcion())
                .orden(request.getOrden() != null ? request.getOrden() : 0)
                .activo(request.getActivo() != null ? request.getActivo() : true)
                .build();
        seccion = galeriaSeccionRepository.save(seccion);
        return toDtoSinFotos(seccion);
    }

    @Transactional
    public GaleriaSeccionDto actualizar(Long id, GaleriaSeccionRequest request) {
        GaleriaSeccion seccion = galeriaSeccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GaleriaSeccion", id));
        if (request.getTitulo() != null) seccion.setTitulo(request.getTitulo());
        if (request.getDescripcion() != null) seccion.setDescripcion(request.getDescripcion());
        if (request.getOrden() != null) seccion.setOrden(request.getOrden());
        if (request.getActivo() != null) seccion.setActivo(request.getActivo());
        seccion = galeriaSeccionRepository.save(seccion);
        return toDtoSinFotos(seccion);
    }

    @Transactional
    public void eliminar(Long id) {
        GaleriaSeccion seccion = galeriaSeccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GaleriaSeccion", id));
        for (Foto foto : fotoRepository.findBySeccion_Id(seccion.getId())) {
            foto.setSeccion(null);
        }
        galeriaSeccionRepository.delete(seccion);
    }

    private GaleriaSeccionDto toDtoConFotos(GaleriaSeccion s) {
        List<FotoDto> fotosDto = fotoRepository.findBySeccion_IdAndActivoTrueOrderByFechaCreacionDesc(s.getId()).stream()
                .map(this::fotoToDto)
                .collect(Collectors.toList());
        return GaleriaSeccionDto.builder()
                .id(s.getId())
                .titulo(s.getTitulo())
                .descripcion(s.getDescripcion())
                .orden(s.getOrden())
                .activo(s.getActivo())
                .fechaCreacion(s.getFechaCreacion())
                .fotos(fotosDto)
                .build();
    }

    private GaleriaSeccionDto toDtoSinFotos(GaleriaSeccion s) {
        return GaleriaSeccionDto.builder()
                .id(s.getId())
                .titulo(s.getTitulo())
                .descripcion(s.getDescripcion())
                .orden(s.getOrden())
                .activo(s.getActivo())
                .fechaCreacion(s.getFechaCreacion())
                .build();
    }

    private FotoDto fotoToDto(Foto f) {
        return FotoDto.builder()
                .id(f.getId())
                .titulo(f.getTitulo())
                .descripcion(f.getDescripcion())
                .urlImagen(f.getUrlImagen())
                .activo(f.getActivo())
                .fechaCreacion(f.getFechaCreacion())
                .seccionId(f.getSeccion() != null ? f.getSeccion().getId() : null)
                .build();
    }
}
