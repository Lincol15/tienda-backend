package com.example.caporalescristos.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoRequest {

    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String urlImagen;
    private Long categoriaId;
    private Integer stock;
    private Boolean activo = true;
}
