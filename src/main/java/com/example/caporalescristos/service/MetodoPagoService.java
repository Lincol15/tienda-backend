package com.example.caporalescristos.service;

import com.example.caporalescristos.dto.MetodoPagoDto;
import com.example.caporalescristos.dto.MetodoPagoRequest;
import com.example.caporalescristos.entity.MetodoPago;
import com.example.caporalescristos.exception.ResourceNotFoundException;
import com.example.caporalescristos.repository.MetodoPagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetodoPagoService {

    private final MetodoPagoRepository metodoPagoRepository;

    @Transactional(readOnly = true)
    public List<MetodoPagoDto> listarActivos() {
        return metodoPagoRepository.findByActivoTrueOrderByOrdenAsc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MetodoPagoDto> listarTodosAdmin() {
        return metodoPagoRepository.findAllByOrderByOrdenAsc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MetodoPagoDto obtenerPorIdAdmin(Long id) {
        MetodoPago m = metodoPagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MetodoPago", id));
        return toDto(m);
    }

    @Transactional
    public MetodoPagoDto crear(MetodoPagoRequest request) {
        MetodoPago metodo = MetodoPago.builder()
                .nombre(request.getNombre())
                .tipo(request.getTipo() != null ? request.getTipo() : "OTRO")
                .valor(request.getValor())
                .imagenUrl(request.getImagenUrl())
                .activo(request.getActivo() != null ? request.getActivo() : true)
                .orden(request.getOrden() != null ? request.getOrden() : 0)
                .build();
        metodo = metodoPagoRepository.save(metodo);
        return toDto(metodo);
    }

    @Transactional
    public MetodoPagoDto actualizar(Long id, MetodoPagoRequest request) {
        MetodoPago metodo = metodoPagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MetodoPago", id));
        if (request.getNombre() != null) metodo.setNombre(request.getNombre());
        if (request.getTipo() != null) metodo.setTipo(request.getTipo());
        if (request.getValor() != null) metodo.setValor(request.getValor());
        if (request.getImagenUrl() != null) metodo.setImagenUrl(request.getImagenUrl());
        if (request.getActivo() != null) metodo.setActivo(request.getActivo());
        if (request.getOrden() != null) metodo.setOrden(request.getOrden());
        metodo = metodoPagoRepository.save(metodo);
        return toDto(metodo);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!metodoPagoRepository.existsById(id)) {
            throw new ResourceNotFoundException("MetodoPago", id);
        }
        metodoPagoRepository.deleteById(id);
    }

    private MetodoPagoDto toDto(MetodoPago m) {
        return MetodoPagoDto.builder()
                .id(m.getId())
                .nombre(m.getNombre())
                .tipo(m.getTipo())
                .valor(m.getValor())
                .imagenUrl(m.getImagenUrl())
                .activo(m.getActivo())
                .orden(m.getOrden())
                .build();
    }
}
