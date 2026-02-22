package com.example.caporalescristos.dto;

import lombok.Data;

@Data
public class MetodoPagoRequest {

    private String nombre;
    private String tipo;
    private String valor;
    private String imagenUrl;
    private Boolean activo = true;
    private Integer orden = 0;
}
