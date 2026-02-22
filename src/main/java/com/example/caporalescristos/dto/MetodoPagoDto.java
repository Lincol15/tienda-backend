package com.example.caporalescristos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetodoPagoDto {

    private Long id;
    private String nombre;
    private String tipo;
    private String valor;
    private String imagenUrl;
    private Boolean activo;
    private Integer orden;
}
