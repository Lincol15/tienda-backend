package com.example.caporalescristos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GaleriaSeccionDto {

    private Long id;
    private String titulo;
    private String descripcion;
    private Integer orden;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    /** Solo en respuesta pública: fotos activas de esta sección */
    private List<FotoDto> fotos;
}
