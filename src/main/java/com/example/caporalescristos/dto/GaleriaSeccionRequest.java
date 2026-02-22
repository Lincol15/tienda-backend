package com.example.caporalescristos.dto;

import lombok.Data;

@Data
public class GaleriaSeccionRequest {

    private String titulo;
    private String descripcion;
    private Integer orden;
    private Boolean activo = true;
}
