package com.example.caporalescristos.service;

import com.example.caporalescristos.dto.ConfiguracionTiendaDto;
import com.example.caporalescristos.dto.ConfiguracionTiendaRequest;
import com.example.caporalescristos.entity.ConfiguracionTienda;
import com.example.caporalescristos.repository.ConfiguracionTiendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfiguracionTiendaService {

    private final ConfiguracionTiendaRepository configuracionTiendaRepository;

    @Transactional(readOnly = true)
    public ConfiguracionTiendaDto obtener() {
        return configuracionTiendaRepository.findAll().stream()
                .findFirst()
                .map(this::toDto)
                .orElse(ConfiguracionTiendaDto.builder().id(null).whatsappNumero(null).build());
    }

    @Transactional
    public ConfiguracionTiendaDto actualizar(ConfiguracionTiendaRequest request) {
        ConfiguracionTienda config = configuracionTiendaRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> {
                    ConfiguracionTienda nueva = ConfiguracionTienda.builder().whatsappNumero(null).build();
                    return configuracionTiendaRepository.save(nueva);
                });
        if (request.getWhatsappNumero() != null) {
            config.setWhatsappNumero(request.getWhatsappNumero().trim().isEmpty() ? null : request.getWhatsappNumero().trim());
        }
        config = configuracionTiendaRepository.save(config);
        return toDto(config);
    }

    private ConfiguracionTiendaDto toDto(ConfiguracionTienda c) {
        return ConfiguracionTiendaDto.builder()
                .id(c.getId())
                .whatsappNumero(c.getWhatsappNumero())
                .build();
    }
}
