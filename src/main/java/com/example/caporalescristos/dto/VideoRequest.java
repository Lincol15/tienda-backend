package com.example.caporalescristos.dto;

import lombok.Data;

@Data
public class VideoRequest {

    private String titulo;
    private String descripcion;
    /** URL del video (obligatoria si no se sube archivo; opcional si se sube part "video") */
    private String urlVideo;
    private Boolean activo = true;
}
