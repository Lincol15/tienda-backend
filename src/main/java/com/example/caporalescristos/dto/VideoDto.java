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
public class VideoDto {

    private Long id;
    private String titulo;
    private String descripcion;
    private String urlVideo;
    /** Tipo de URL: YOUTUBE, FACEBOOK, DIRECTO. El frontend lo usa para decidir si incrustar o mostrar enlace. */
    private String tipoVideo;
    /** URL lista para embed (YouTube) o reproducción directa; null si es Facebook (no se puede incrustar). */
    private String embedUrl;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
