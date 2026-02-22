package com.example.caporalescristos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FotoDto {

    private Long id;
    private String titulo;
    private String descripcion;
    private String urlImagen;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    /** ID de la sección de galería a la que pertenece (null = sin sección) */
    private Long seccionId;
}
