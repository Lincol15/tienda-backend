package com.example.caporalescristos.dto;

import lombok.Data;

@Data
public class FotoRequest {

    private String titulo;
    private String descripcion;
    private Long seccionId;
    private Boolean activo = true;
}
