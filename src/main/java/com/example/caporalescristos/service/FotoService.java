package com.example.caporalescristos.service;

import com.example.caporalescristos.dto.FotoDto;
import com.example.caporalescristos.dto.FotoRequest;
import com.example.caporalescristos.entity.Foto;
import com.example.caporalescristos.entity.GaleriaSeccion;
import com.example.caporalescristos.exception.ResourceNotFoundException;
import com.example.caporalescristos.repository.FotoRepository;
import com.example.caporalescristos.repository.GaleriaSeccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FotoService {

    private static final String CLOUDINARY_FOLDER = "caporales/galeria/fotos";

    private final FotoRepository fotoRepository;
    private final GaleriaSeccionRepository galeriaSeccionRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional(readOnly = true)
    public List<FotoDto> listarPublicas() {
        return fotoRepository.findByActivoTrueOrderByFechaCreacionDesc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FotoDto obtenerPorId(Long id) {
        Foto foto = fotoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Foto", id));
        if (!foto.getActivo()) {
            throw new ResourceNotFoundException("Foto", id);
        }
        return toDto(foto);
    }

    @Transactional(readOnly = true)
    public List<FotoDto> listarTodasAdmin() {
        return fotoRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FotoDto obtenerPorIdAdmin(Long id) {
        Foto foto = fotoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Foto", id));
        return toDto(foto);
    }

    @Transactional
    public FotoDto crear(FotoRequest request, MultipartFile imagen) {
        String urlImagen = cloudinaryService.uploadImage(imagen, CLOUDINARY_FOLDER);
        if (urlImagen == null) {
            throw new IllegalArgumentException("Se requiere una imagen");
        }
        GaleriaSeccion seccion = request.getSeccionId() != null
                ? galeriaSeccionRepository.findById(request.getSeccionId()).orElse(null)
                : null;
        Foto foto = Foto.builder()
                .titulo(request.getTitulo())
                .descripcion(request.getDescripcion())
                .urlImagen(urlImagen)
                .seccion(seccion)
                .activo(request.getActivo() != null ? request.getActivo() : true)
                .build();
        foto = fotoRepository.save(foto);
        return toDto(foto);
    }

    @Transactional
    public FotoDto actualizar(Long id, FotoRequest request, MultipartFile imagenNueva) {
        Foto foto = fotoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Foto", id));
        if (request.getTitulo() != null) foto.setTitulo(request.getTitulo());
        if (request.getDescripcion() != null) foto.setDescripcion(request.getDescripcion());
        if (request.getActivo() != null) foto.setActivo(request.getActivo());
        if (request.getSeccionId() != null) {
            GaleriaSeccion seccion = galeriaSeccionRepository.findById(request.getSeccionId()).orElse(null);
            foto.setSeccion(seccion);
        }
        if (imagenNueva != null && !imagenNueva.isEmpty()) {
            foto.setUrlImagen(cloudinaryService.uploadImage(imagenNueva, CLOUDINARY_FOLDER));
        }
        foto = fotoRepository.save(foto);
        return toDto(foto);
    }

    @Transactional
    public void eliminar(Long id) {
        Foto foto = fotoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Foto", id));
        fotoRepository.delete(foto);
    }

    private FotoDto toDto(Foto f) {
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
